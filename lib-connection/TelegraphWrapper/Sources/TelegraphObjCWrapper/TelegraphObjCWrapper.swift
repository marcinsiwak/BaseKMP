import Foundation
import Telegraph

/// Objective-C compatible wrapper for a Telegraph WebSocket connection.
@objc public class TGWebSocket: NSObject {

  private let webSocket: WebSocket

  init(_ webSocket: WebSocket) {
    self.webSocket = webSocket
  }

  /// Send a text message to this WebSocket client.
  @objc public func sendText(_ text: String) {
    webSocket.send(text: text)
  }

  /// Send binary data to this WebSocket client.
  @objc public func sendData(_ data: Data) {
    webSocket.send(data: data)
  }

  /// Close the WebSocket connection.
  @objc public func close() {
    webSocket.close(immediately: false)
  }

  /// Close the WebSocket connection immediately.
  @objc public func closeImmediately() {
    webSocket.close(immediately: true)
  }
}

@objc public protocol TGServerWebSocketDelegate: AnyObject {

  /// Called when a new WebSocket client connects.
  /// - Parameters:
  ///   - server: The server instance.
  ///   - webSocket: The newly connected WebSocket wrapper.
  ///   - path: The request path from the HTTP upgrade handshake.
  func telegraphServer(_ server: TGServer,
                       webSocketDidConnect webSocket: TGWebSocket,
                       path: String,
                       id: String)

  /// Called when a WebSocket client disconnects.
  /// - Parameters:
  ///   - server: The server instance.
  ///   - webSocket: The WebSocket wrapper that disconnected.
  ///   - error: An error if the disconnection was abnormal, otherwise nil.
  func telegraphServer(_ server: TGServer,
                       webSocketDidDisconnect webSocket: TGWebSocket,
                       error: Error?)

  /// Called when a text message is received from a WebSocket client.
  /// - Parameters:
  ///   - server: The server instance.
  ///   - webSocket: The WebSocket wrapper that sent the message.
  ///   - text: The received text string.
  func telegraphServer(_ server: TGServer,
                       webSocket: TGWebSocket,
                       didReceiveText text: String)

  /// Called when a binary message is received from a WebSocket client.
  /// - Parameters:
  ///   - server: The server instance.
  ///   - webSocket: The WebSocket wrapper that sent the message.
  ///   - data: The received binary data.
  @objc optional func telegraphServer(_ server: TGServer,
                                      webSocket: TGWebSocket,
                                      didReceiveData data: Data)
}

@objc public class TGServer: NSObject {

  // MARK: Private state

  private var server: Server

  /// Maps a wrapped TGWebSocket back to the underlying WebSocket so we can look
  /// it up quickly in delegate callbacks.
  private var socketMap: [ObjectIdentifier: TGWebSocket] = [:]
  private let socketMapQueue = DispatchQueue(label: "TGServer.socketMap", attributes: .concurrent)

  // MARK: Public properties

  /// Delegate that receives WebSocket lifecycle and message events.
  @objc public weak var webSocketDelegate: TGServerWebSocketDelegate?
    

  /// The port the server is currently listening on, or 0 if not started.
  @objc public var port: Int {
    return Int(server.port ?? 0)
  }

  /// Whether the server is currently running.
  @objc public var isRunning: Bool {
    return server.isRunning
  }

  // MARK: - Initializers

  /// Create an unsecure (plain HTTP) server.
  @objc public override init() {
    server = Server()
    super.init()
    setupWebSocketDelegate()
  }

  /// Create a secure (HTTPS/TLS) server.
  /// - Parameters:
  ///   - p12URL: URL to a PKCS12 (.p12) file containing the server identity.
  ///   - p12Passphrase: Passphrase for the P12 file.
  ///   - caDerURL: URL to a DER-encoded Certificate Authority certificate.
  @objc public init?(p12URL: URL, p12Passphrase: String, caDerURL: URL) {
    guard
      let identity = CertificateIdentity(p12URL: p12URL, passphrase: p12Passphrase),
      let caCert   = Certificate(derURL: caDerURL)
    else {
      return nil
    }
    server = Server(identity: identity, caCertificates: [caCert])
    super.init()
    setupWebSocketDelegate()
  }

  // MARK: - Start / Stop

  /// Start the server on the given port.
  /// - Parameters:
  ///   - port: TCP port to listen on (must be > 1024 without root access).
  ///   - error: Set on failure.
  /// - Returns: `YES` on success, `NO` on failure.
  @objc @discardableResult
  public func start(onPort port: Int, error: NSErrorPointer) -> Bool {
    do {
        try server.start(port: Endpoint.Port(UInt16(port)))
      return true
    } catch let err {
      error?.pointee = err as NSError
      return false
    }
  }

  /// Start the server bound to a specific network interface (e.g., `"localhost"`).
  @objc @discardableResult
  public func start(onPort port: Int, interface: String, error: NSErrorPointer) -> Bool {
    do {
        try server.start(port: Endpoint.Port(UInt16(port)), interface: interface)
      return true
    } catch let err {
      error?.pointee = err as NSError
      return false
    }
  }

  /// Stop the server and close all connections.
  @objc public func stop() {
    server.stop()
    socketMapQueue.async(flags: .barrier) { self.socketMap.removeAll() }
  }
    
    @objc public func setConcurrency(concurencyNumber: Int) {
        server.concurrency = concurencyNumber
    }

  // MARK: - HTTP Routes

  /// Register an HTTP route.
  ///
  /// - Parameters:
  ///   - method: HTTP method string, e.g. `"GET"`, `"POST"`, `"PUT"`, `"DELETE"`.
  ///   - path: Route path, supports `:param` and `*` wildcards, e.g. `"hello/:name"`.
  ///   - handler: Block called on a matching request. Receives a params dictionary
  ///     and the raw body data. Must return a dictionary with keys:
  ///     - `"statusCode"` (`NSNumber`, required) – HTTP status code.
  ///     - `"body"` (`NSString`, optional)       – Response body text.
  @objc public func addRoute(forMethod method: String,
                              path: String,
                              handler: @escaping (_ params: [String: String],
                                                  _ body: Data?) -> [String: Any]) {
      let httpMethod = HTTPMethod.init(stringLiteral: method)
    server.route(httpMethod, path) { request -> HTTPResponse in
      let params = request.params
      let body   = request.body.isEmpty ? nil : request.body
      let result = handler(params, body)

      let code   = (result["statusCode"] as? Int) ?? 200
      let text   = result["body"] as? String ?? ""
        let status = HTTPStatus(code: code, phrase: "status") // check if its correct
      return HTTPResponse(status, content: text)
    }
  }

  /// Convenience: register a GET route that returns a plain-text response.
  @objc public func addGET(_ path: String,
                            handler: @escaping (_ params: [String: String]) -> String) {
    server.route(.GET, path) { request -> HTTPResponse in
      let text = handler(request.params)
      return HTTPResponse(.ok, content: text)
    }
  }

  /// Convenience: register a POST route. Body is delivered as UTF-8 string.
  @objc public func addPOST(_ path: String,
                              handler: @escaping (_ params: [String: String],
                                                  _ bodyString: String?) -> String) {
    server.route(.POST, path) { request -> HTTPResponse in
      let bodyStr = request.body.isEmpty
        ? nil
        : String(data: request.body, encoding: .utf8)
      let text = handler(request.params, bodyStr)
      return HTTPResponse(.ok, content: text)
    }
  }

  // MARK: - WebSocket: broadcast helpers

  /// Send a text message to all currently-connected WebSocket clients.
  @objc public func broadcastText(_ text: String) {
    socketMapQueue.sync {
      socketMap.values.forEach { $0.sendText(text) }
    }
  }

  /// Send binary data to all currently-connected WebSocket clients.
  @objc public func broadcastData(_ data: Data) {
    socketMapQueue.sync {
      socketMap.values.forEach { $0.sendData(data) }
    }
  }

  // MARK: - Private helpers

  private func setupWebSocketDelegate() {
    server.webSocketDelegate = self
  }

  private func wrappedSocket(for webSocket: WebSocket) -> TGWebSocket {
    let key = ObjectIdentifier(webSocket as AnyObject)
    return socketMapQueue.sync { socketMap[key] } ?? {
      let wrapped = TGWebSocket(webSocket)
      socketMapQueue.async(flags: .barrier) { self.socketMap[key] = wrapped }
      return wrapped
    }()
  }

  private func removeSocket(for webSocket: WebSocket) {
    let key = ObjectIdentifier(webSocket as AnyObject)
    socketMapQueue.async(flags: .barrier) { self.socketMap.removeValue(forKey: key) }
  }
}

// MARK: - ServerWebSocketDelegate (Telegraph internal)

extension TGServer: ServerWebSocketDelegate {

  public func server(_ server: Server,
                     webSocketDidConnect webSocket: WebSocket,
                     handshake: HTTPRequest) {
    let wrapped  = TGWebSocket(webSocket)
    let key      = ObjectIdentifier(webSocket as AnyObject)
    socketMapQueue.async(flags: .barrier) { self.socketMap[key] = wrapped }

    let path = handshake.uri.path
    let id = handshake.uri.queryItems?.first(where: { $0.name == "id" })?.value ?? ""
    DispatchQueue.main.async {
      self.webSocketDelegate?.telegraphServer(self,
                                              webSocketDidConnect: wrapped,
                                              path: path,
                                              id: id)
    }
  }

  public func server(_ server: Server,
                     webSocketDidDisconnect webSocket: WebSocket,
                     error: Error?) {
    let wrapped = wrappedSocket(for: webSocket)
    removeSocket(for: webSocket)
    DispatchQueue.main.async {
      self.webSocketDelegate?.telegraphServer(self,
                                              webSocketDidDisconnect: wrapped,
                                              error: error)
    }
  }

  public func server(_ server: Server,
                     webSocket: WebSocket,
                     didReceiveMessage message: WebSocketMessage) {
    let wrapped = wrappedSocket(for: webSocket)
    switch message.payload {
    case .text(let text):
      DispatchQueue.main.async {
        self.webSocketDelegate?.telegraphServer(self,
                                                webSocket: wrapped,
                                                didReceiveText: text)
      }
    case .binary(let data):
      DispatchQueue.main.async {
        self.webSocketDelegate?.telegraphServer?(self,
                                                 webSocket: wrapped,
                                                 didReceiveData: data)
      }
    default:
      break
    }
  }

  public func server(_ server: Server,
                     webSocket: WebSocket,
                     didSendMessage message: WebSocketMessage) {
    // No-op — expose via delegate extension if needed.
  }
}
