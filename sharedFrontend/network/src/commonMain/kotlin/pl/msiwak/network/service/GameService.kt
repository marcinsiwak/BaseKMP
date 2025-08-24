package pl.msiwak.network.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.withContext
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.common.model.dispatcher.Dispatchers
import pl.msiwak.network.ConnectionManager
import pl.msiwak.network.KtorClient
import pl.msiwak.network.KtorServer

class GameService(
    private val ktorServer: KtorServer,
    private val ktorClient: KtorClient,
    private val connectionManager: ConnectionManager
) {

    suspend fun observePlayersConnection(): Flow<WebSocketEvent.PlayerConnection> = withContext(Dispatchers.IO) {
        ktorClient.observeWebSocketEvents().filterIsInstance<WebSocketEvent.PlayerConnection>()
    }

    suspend fun startGame() {
        val ipAddress = connectionManager.getLocalIpAddress() ?: throw Exception("Cannot get local IP address")
        ktorServer.startServer(ipAddress, 53287)
    }

    suspend fun stopGame() {
        ktorServer.stopServer()
    }
}
