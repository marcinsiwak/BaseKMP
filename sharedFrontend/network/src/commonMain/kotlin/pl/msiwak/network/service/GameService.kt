package pl.msiwak.network.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.msiwak.common.model.Player
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.common.model.dispatcher.Dispatchers
import pl.msiwak.network.ConnectionManager
import pl.msiwak.network.KtorClient
import pl.msiwak.network.ServerManager

private const val PORT = 63287

class GameService(
    private val serverManager: ServerManager,
    private val ktorClient: KtorClient,
    private val connectionManager: ConnectionManager
) {
    private var scope = CoroutineScope(Dispatchers.IO)

    private lateinit var deviceIpId: String

    suspend fun observePlayersConnection(): Flow<WebSocketEvent> = withContext(Dispatchers.IO) {
        ktorClient.webSocketEvent.filterIsInstance<WebSocketEvent>()
    }

    suspend fun sendClientEvent(webSocketEvent: WebSocketEvent) = withContext(Dispatchers.IO) {
        ktorClient.send(webSocketEvent)
    }

    suspend fun findGame(): String? = withContext(Dispatchers.IO) {
        return@withContext connectionManager.findGame(port = PORT)
    }

    suspend fun connectPlayer(host: String, playerName: String) {
        deviceIpId = connectionManager.getLocalIpAddress()?.substringAfterLast(".")
            ?: throw Exception("Cannot get local IP address")
        val player = Player(id = deviceIpId, name = playerName)
        ktorClient.connect(host = host, port = PORT, player = player)
    }

    suspend fun disconnectPlayer() = withContext(Dispatchers.IO) {
        val deviceIpId = connectionManager.getLocalIpAddress()?.substringAfterLast(".")
            ?: throw Exception("Cannot get local IP address")

        ktorClient.disconnect(deviceIpId)
    }

    suspend fun createGame() = withContext(Dispatchers.IO) {
        if (!scope.isActive) {
            scope = CoroutineScope(Dispatchers.IO)
        }
        scope.launch {
            val ipAddress = connectionManager.getLocalIpAddress() ?: throw Exception("Cannot get local IP address")
            serverManager.startServer(ipAddress, PORT)
            serverManager.observeMessages()
        }
    }

    fun getUserId(): String = deviceIpId

    suspend fun finishGame() = withContext(Dispatchers.IO) {
        serverManager.stopServer()
        scope.cancel()
    }
}
