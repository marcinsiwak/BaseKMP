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

class KtorServerImpl: KtorServer {

    var httpServer: HttpServer = HttpServer()

    func startServer() {
        httpServer.start()
    }
    
    func stopServer() {
        httpServer.server.stop()
    }
    

}

public class HttpServer: NSObject {
    
    var server: Server!
    var websocketClient: WebSocketClient!
    let PORT:Int = 3000
}

public extension HttpServer {
    
    func start(){
        DispatchQueue.global().async {
            self.setupServer()
        }
    }
    
    func setupServer(){
        
        self.server = Server()
        
        server.delegate = self
        server.webSocketDelegate = self
        // Can handle upto 5 requests concurrently
        server.concurrency = 5
        
        do {
            try server.start(port:self.PORT)
        } catch {
            print("Error when starting error:" ,error.localizedDescription)
        }
        
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
    }
    
    public func server(_ server: Telegraph.Server, webSocketDidConnect webSocket: any Telegraph.WebSocket, handshake: Telegraph.HTTPRequest) {
        print("Websocket client connected")
    }
    
    
    public func server(_ server: Server, webSocket: WebSocket, didReceiveMessage message: WebSocketMessage) {
      print("WebSocket message received:", message)
    }

    
    public func server(_ server: Server, webSocket: WebSocket, didSendMessage message: WebSocketMessage) {
      webSocket.send(text: "Hello from websocket!")
      print("WebSocket message sent:", message)
    }
    
}
