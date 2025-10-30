package pl.msiwak.network.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.msiwak.common.error.GameNotFoundException
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.Player
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.common.model.dispatcher.Dispatchers
import pl.msiwak.network.ConnectionManager
import pl.msiwak.network.KtorClient
import pl.msiwak.network.ServerManager

private const val PORT = 53287

class GameService(
    private val serverManager: ServerManager,
    private val ktorClient: KtorClient,
    private val connectionManager: ConnectionManager
) {
    private var scope = CoroutineScope(Dispatchers.IO)

    private lateinit var deviceIpId: String

    private var serverIp: String? = null

    suspend fun observeWebSocketEvents(): Flow<WebSocketEvent> = withContext(Dispatchers.IO) {
        ktorClient.webSocketEvent.filterIsInstance<WebSocketEvent>()
    }

    suspend fun sendClientEvent(webSocketEvent: WebSocketEvent) = withContext(Dispatchers.IO) {
        ktorClient.send(webSocketEvent)
    }

    fun findGame(): Flow<String?> = flow<String?> {
//        if (serverIp != null) {
//            emit(serverIp)
//            return@flow
//        }
        emit(connectionManager.findGame(port = PORT) ?: throw GameNotFoundException())
        delay(1000)
    }
        .retryWhen { cause, attempt ->
            cause is GameNotFoundException && attempt < 5
        }
        .catch { emit(null) }
        .onEach { serverIp = it }
        .flowOn(Dispatchers.IO)

    suspend fun connectPlayer(playerName: String) = withContext(Dispatchers.IO) {
        deviceIpId = connectionManager.getLocalIpAddress()?.substringAfterLast(".")
            ?: throw Exception("Cannot get local IP address")
        val ip = serverIp ?: throw GameNotFoundException()
        scope.launch {
            connectPlayerToGame(playerName, ip)
        }
    }

    private suspend fun connectPlayerToGame(playerName: String, ip: String) {
        val player = Player(id = deviceIpId, name = playerName, isActive = true)
        ktorClient.connect(host = ip, port = PORT, player = player)
    }

    suspend fun disconnectPlayer() = withContext(Dispatchers.IO) {
        val deviceIpId = connectionManager.getLocalIpAddress()?.substringAfterLast(".")
            ?: throw Exception("Cannot get local IP address")

        ktorClient.disconnect(deviceIpId)
    }

    suspend fun createGame(adminName: String, gameSession: GameSession? = null) = withContext(Dispatchers.IO) {
        if (!scope.isActive) {
            scope = CoroutineScope(Dispatchers.IO)
        }
        val ipAddress = connectionManager.getLocalIpAddress() ?: throw Exception("Cannot get local IP address")
        scope.launch {
            launch { serverManager.startServer(ipAddress, PORT) }
            launch { serverManager.observeMessages() }
            launch { serverManager.observeGameSession() }
            launch {
                delay(100)
                createGameAndConnect(ipAddress, adminName, gameSession)
            }
        }
    }

    private suspend fun createGameAndConnect(ipAddress: String, adminName: String, gameSession: GameSession?) {
        deviceIpId = ipAddress.substringAfterLast(".")
        serverManager.createGame(deviceIpId, ipAddress, gameSession)
        connectPlayerToGame(adminName, ipAddress)
    }

    fun getUserId(): String = deviceIpId

//    suspend fun finishGame() = withContext(Dispatchers.IO) {
//        serverManager.stopServer()
//        scope.cancel()
//    }
}
