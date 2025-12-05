//
//  KtorServerImpl.swift
//  iosApp
//
//  Created by Marcin Siwak on 22/08/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import ComposeApp
import Telegraph
import Combine
import FirebaseAnalytics
import FirebaseCrashlytics

class KtorServerImpl: KtorServer {

    private let subject = PassthroughSubject<String, Never>()

    lazy var messages: Kotlinx_coroutines_coreFlow = {
        subject.asFlow()
    }()

    lazy var httpServer: HttpServer = {
        HttpServer(subject: subject)
    }()

    func startServer(host: String, port: Int32) async throws {
        if (httpServer.server != nil && httpServer.server.isRunning) {
            return
        }
        // Analytics.logEvent("test_connection", parameters: ["serverStarted": true])
        subject.send("Server started")
        httpServer.start(host: host, port: port)
    }

    func stopServer() async throws {
        if (httpServer.server != nil) {
            Analytics.logEvent("test_connection", parameters: ["serverStoped": true])
            httpServer.server.stop(immediately: true)
        }
    }

    func sendMessage(userId: String, message: String) async throws {
        httpServer.sendMessage(userId: userId, message: message)
    }

    func closeSocket(userId: String) async throws {
        httpServer.closeSocket(userId: userId)
    }

    func sendMessageToAll(message: String) async throws {
        httpServer.sendMessageToAll(message: message)
    }

    func closeAllSockets() async throws {
        httpServer.closeAllSockets()
    }

    func isRunning() -> Bool {
        return httpServer.server != nil && httpServer.server.isRunning
    }
}

public class HttpServer: NSObject {

    var sockets: [String: Telegraph.WebSocket] = [:]

    var server: Server!
    var websocketClient: WebSocketClient!
    let PORT: Int = 3000

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

    func setupServer(port: Int32) {
        self.server = Server()

        server.delegate = self
        server.webSocketDelegate = self
        // Can handle upto 5 requests concurrently
        server.concurrency = 5

        do {
            try server.start(port: Int(port))
        } catch {
            FirebaseCrashlytics.Crashlytics.crashlytics().record(error: error)
            print("Error when starting error:", error.localizedDescription)
        }

    }

    func sendMessage(userId: String, message: String) {
        sockets[userId]?.send(text: message)
    }

    func sendMessageToAll(message: String) {
        print("message to all \(sockets)")
        sockets.values.forEach { socket in
            socket.send(text: message)
        }
    }

    func closeSocket(userId: String) {
        if let socket = sockets[userId] {
            socket.close(immediately: false)               // graceful close (code 1000)
            sockets.removeValue(forKey: userId)
        }
    }

    func closeAllSockets() {
        sockets.forEach { (key: String, socket: any WebSocket) in
            socket.close(immediately: false)
            sockets.removeValue(forKey: key)
        }
    }
}


extension HttpServer: ServerDelegate {
    public func serverDidStop(_ server: Telegraph.Server, error: (any Error)?) {
        if(error != nil) {
            FirebaseCrashlytics.Crashlytics.crashlytics().record(error: error!)
        }

        print("Server stopped:", error?.localizedDescription ?? "Unknown")
    }
}

extension HttpServer: ServerWebSocketDelegate {

    public func server(_ server: Telegraph.Server, webSocketDidDisconnect webSocket: any Telegraph.WebSocket, error: (any Error)?) {
        print("Websocket client disconnected \(webSocket)")
        print("Websocket client disconnected \(webSocket)")
        if(error != nil) {
            FirebaseCrashlytics.Crashlytics.crashlytics().record(error: error!)
        }
        if let key = sockets.first(where: { $0.value === webSocket })?.key {
            sockets.removeValue(forKey: key)
            subject?.send("Client disconnected: \(key)")
        }
    }

    public func server(_ server: Telegraph.Server, webSocketDidConnect webSocket: any Telegraph.WebSocket, handshake: Telegraph.HTTPRequest) {
        guard let id = handshake.uri.queryItems?.first(where: { $0.name == "id" })?.value else {
            print("Websocket connection rejected: missing id")
            return
        }
        Analytics.logEvent("test_connection", parameters: ["client_connected": true])
        print("Websocket client connected:", id)
        sockets[id] = webSocket
    }


    public func server(_ server: Server, webSocket: WebSocket, didReceiveMessage message: WebSocketMessage) {
        print("WebSocket message received:", message)
        // Analytics.logEvent("test_connection", parameters: ["websocket_received": message])

        let payload = message.payload
        if case let .text(message) = payload {
            subject?.send(message)
        }
    }


    public func server(_ server: Server, webSocket: WebSocket, didSendMessage message: WebSocketMessage) {
        print("WebSocket message sent:", message)
    }

}
