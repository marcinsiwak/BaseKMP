package pl.msiwak.network.service

import pl.msiwak.network.KtorServer

class GameService(private val ktorServer: KtorServer) {
    suspend fun startGame() {
        ktorServer.startServer()
    }
    suspend fun stopGame() {
        ktorServer.stopServer()
    }
}
