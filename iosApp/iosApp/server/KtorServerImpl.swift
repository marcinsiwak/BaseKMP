//
//  KtorServerImpl.swift
//  iosApp
//
//  Created by Marcin Siwak on 22/08/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import sharedFrontend
import ComposeApp
import Telegraph
import Combine

class KtorServerImpl: KtorServer {

    private let subject = PassthroughSubject<String, Never>()

    lazy var messages: Kotlinx_coroutines_coreFlow = {
           subject.asFlow()
       }()

    lazy var httpServer: HttpServer = {
        HttpServer(subject: subject)
    }()

    func startServer(host: String, port: Int32) {
        httpServer.start(host: host, port: port)
    }
    
    func stopServer() {
        httpServer.server.stop()
    }
    
    func sendMessage(userId: String, message: String) async throws {
        httpServer.sendMessage(userId: userId, message: message)
    }

    func closeSocker(userId: String) async throws {
        httpServer.closeSocket(userId: userId)
    }

    func sendMessageToAll(message: String) async throws {
        httpServer.sendMessageToAll(message: message)
    }
}

public class HttpServer: NSObject {
    
    var sockets: [String: Telegraph.WebSocket] = [:]

    var server: Server!
    var websocketClient: WebSocketClient!
    let PORT:Int = 3000

    var subject: PassthroughSubject<String, Never>?

    init(subject: PassthroughSubject<String, Never>? = nil) {
        self.subject = subject
    }
}

public extension HttpServer {
    
    func start(host: String, port: Int32) {
        DispatchQueue.global().async {
            self.setupServer(port: port)
        }
    }
    
    func setupServer(port: Int32){
        
        self.server = Server()
        
        server.delegate = self
        server.webSocketDelegate = self
        // Can handle upto 5 requests concurrently
        server.concurrency = 5
        
        do {
            try server.start(port: Int(port))
        } catch {
            print("Error when starting error:" ,error.localizedDescription)
        }
        
    }
    
    func sendMessage(userId: String, message: String) {
        sockets[userId]?.send(text: message)
    }
    
    func sendMessageToAll(message: String) {
        sockets.values.forEach { socket in
            socket.send(text: message)
        }
    }

    func closeSocket(userId: String) {
        sockets[userId]?.close(immediately: true)
    }
}


extension HttpServer: ServerDelegate {
    public func serverDidStop(_ server: Telegraph.Server, error: (any Error)?) {
        print("Server stopped:",error?.localizedDescription ?? "Unknown")
    }
}

extension HttpServer: ServerWebSocketDelegate {
    
    public func server(_ server: Telegraph.Server, webSocketDidDisconnect webSocket: any Telegraph.WebSocket, error: (any Error)?) {
        print("Websocket client disconnected")
        if let key = sockets.first(where: { $0.value === webSocket })?.key {
            sockets.removeValue(forKey: key)
        }
    }
    
    public func server(_ server: Telegraph.Server, webSocketDidConnect webSocket: any Telegraph.WebSocket, handshake: Telegraph.HTTPRequest) {
        let id = handshake.uri.queryItems?.first(where: { item in
            item.name == "id"
        })?.value

        let userId = id ?? "0.0.0.0"
        
        if sockets[userId] == nil {
            print("Websocket client connected:", id ?? "Unknown")
            subject?.send("Player connected: \(userId)")
            sockets[userId] = webSocket
        }
        

    }
    
    
    public func server(_ server: Server, webSocket: WebSocket, didReceiveMessage message: WebSocketMessage) {
      print("WebSocket message received:", message)
        let payload = message.payload
        if case let .text(message) = payload {
            subject?.send(message)
        }
    }

    
    public func server(_ server: Server, webSocket: WebSocket, didSendMessage message: WebSocketMessage) {
      print("WebSocket message sent:", message)
    }
    
}




extension Publisher {
    func asFlow<T>() -> Kotlinx_coroutines_coreFlow where Output == T, Failure == Never {
        return Kotlinx_coroutines_coreFlowAdapter(publisher: self)
    }
}

class Kotlinx_coroutines_coreFlowAdapter<T>: Kotlinx_coroutines_coreFlow {
    private var cancellable: AnyCancellable?
    private let publisher: AnyPublisher<T, Never>

    init<P: Publisher>(publisher: P) where P.Output == T, P.Failure == Never {
        self.publisher = publisher.eraseToAnyPublisher()
    }

    func collect(collector: Kotlinx_coroutines_coreFlowCollector, completionHandler: @escaping (Error?) -> Void) {
        cancellable = publisher.sink { value in
            collector.emit(value: value) { _ in }
        }
    }
}
