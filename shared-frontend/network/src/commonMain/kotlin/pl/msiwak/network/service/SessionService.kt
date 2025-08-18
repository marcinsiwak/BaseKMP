package pl.msiwak.network.service

import pl.msiwak.network.KtorServer

class SessionService(private val ktorServer: KtorServer) {
    suspend fun startSession() {
        ktorServer.startServer()
    }
    suspend fun stopSession() {
        ktorServer.stopServer()
    }
}
