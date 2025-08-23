package pl.msiwak.network

import platform.darwin.NSObject

class KtorServerImpl: KtorServer {

    override fun startServer() {
        TODO("Not yet implemented")
    }

    override fun stopServer() {
        TODO("Not yet implemented")
    }

    // class HttpServer: NSObject() {
    //     var server: Server!
    //     var websocketClient: WebSocketClient!
    //     let PORT:Int = 3000
    // }
    // override fun startServer() {}
    // override fun stopServer() {}
}

//public class HttpServer: NSObject {
//
//    var server: Server!
//    var websocketClient: WebSocketClient!
//    let PORT:Int = 3000
//}
//
//// MARK: - Initial server and routes setup
//
//public extension HttpServer {
//
//    func start(){
//        DispatchQueue.global().async {
//            self.setupServer()
//        }
//    }
//
//    func setupServer(){
//
//        // Start the server in HTTP mode (Can also be configured to start in HTTPs mode)
//        self.server = Server()
//
//        server.delegate = self
//        server.webSocketDelegate = self
//
//        server.route(.GET,"/:name",handleGet)
//        server.route(.POST,"/",handlePost)
//        server.route(.PUT,"/:name",handlePut)
//        server.route(.DELETE,"/:name/:age",handleDelete)
//
//        // Can handle upto 5 requests concurrently
//        server.concurrency = 5
//
//        do {
//            try server.start(port:self.PORT) //Start the server in port 3000
//            } catch {
//                print("Error when starting error:",error.localizedDescription)
//            }
//
//        }
//
//}
//
//// MARK: - Server route handlers
//extension HttpServer {
//    func handleGet(request: HTTPRequest) -> HTTPResponse {
//        /// Get the name of the person from the parameter
//        let name = request.params["name"] ?? "stranger"
//
//        return HTTPResponse(content:"Hi \(name)!")
//    }
//
//    func handlePost(request: HTTPRequest) -> HTTPResponse {
//
//        /// Parse the request body
//        let person = try? JSONDecoder().decode(Person.self, from: request.body)
//
//        /// Print the request body to check if the request body is decoded properly
//        print(person ?? "Unknown")
//
//        /// Your modifications...
//
//        /// Return response
//        return HTTPResponse(content:"Hi \(person?.name ?? "Unknown"), your account was created!")
//    }
//
//    func handlePut(request: HTTPRequest) -> HTTPResponse {
//
//        /// Get the name of the person from the parameter
//        let name = request.params["name"] ?? "stranger"
//
//        /// Parse the request body
//        let person = try? JSONDecoder().decode(Person.self, from: request.body)
//
//        /// Print the request body to check if the request body is decoded properly
//        print(person ?? "Unknown")
//
//        /// Your modifications...
//
//        /// Return the response
//        return HTTPResponse(content:"\(name) ,your account data was changed!")
//    }
//
//    func handleDelete(request: HTTPRequest) -> HTTPResponse {
//
//        /// Get the name of the person from the parameter
//        let name = request.params["name"] ?? "stranger"
//
//        /// Your modifications...
//
//        /// Return response
//        return HTTPResponse(content:"\(name) ,your account was deleted.")
//
//    }
//}
//
//// MARK: - Server delegates
//extension HttpServer: ServerDelegate {
//    public func serverDidStop(_ server: Telegraph.Server, error: (any Error)?) {
//        print("Server stopped:",error?.localizedDescription ?? "Unknown")
//    }
//}
//
//extension HttpServer: ServerWebSocketDelegate {
//    public func server(_ server: Telegraph.Server, webSocketDidDisconnect webSocket: any Telegraph.WebSocket, error: (any Error)?) {
//        print("Websocket client disconnected")
//    }
//
//    public func server(_ server: Telegraph.Server, webSocketDidConnect webSocket: any Telegraph.WebSocket, handshake: Telegraph.HTTPRequest) {
//        print("Websocket client connected")
//    }
//
//
//    public func server(_ server: Server, webSocket: WebSocket, didReceiveMessage message: WebSocketMessage) {
//        print("WebSocket message received:", message)
//    }
//
//
//    public func server(_ server: Server, webSocket: WebSocket, didSendMessage message: WebSocketMessage) {
//        webSocket.send(text: "Hello from websocket!")
//        print("WebSocket message sent:", message)
//    }
//
//}
