package pl.msiwak.network.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.msiwak.common.error.LocalIpNotFoundException
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.Player
import pl.msiwak.common.model.dispatcher.Dispatchers
import pl.msiwak.connection.model.WebSocketEvent
import pl.msiwak.network.ConnectionManager
import pl.msiwak.network.KtorClient
import pl.msiwak.network.ServerManager

private const val PORT = 53287

class GameService(
    private val serverManager: ServerManager,
    private val ktorClient: KtorClient,
    private val connectionManager: ConnectionManager
) {
    private lateinit var deviceIpId: String

    suspend fun observeWebSocketEvents(): Flow<WebSocketEvent> = withContext(Dispatchers.IO) {
        ktorClient.webSocketEvent.filterIsInstance<WebSocketEvent>()
    }

    suspend fun sendClientEvent(webSocketEvent: WebSocketEvent) = withContext(Dispatchers.IO) {
        ktorClient.send(webSocketEvent)
    }

    fun connectPlayer(electedIp: String) = CoroutineScope(Dispatchers.IO).launch {
        deviceIpId = getDeviceIp().substringAfterLast(".")
        connectPlayerToGame(electedIp)
    }

    suspend fun getDeviceIp(): String {
        repeat(10) {
            connectionManager.getLocalIpAddress()?.let {
                return it
            }
            delay(1000)
        }
        return connectionManager.getLocalIpAddress() ?: throw LocalIpNotFoundException()
    }


    private suspend fun connectPlayerToGame(ip: String) {
        val player = Player(id = deviceIpId, isActive = true)
        ktorClient.connect(host = ip, port = PORT, player = player)
    }

    suspend fun disconnectPlayer() = withContext(Dispatchers.IO) {
        val deviceIpId = connectionManager.getLocalIpAddress()?.substringAfterLast(".")
            ?: throw Exception("Cannot get local IP address")

        ktorClient.disconnect(deviceIpId)
    }

    private var job: Job? = null

    private var serverJob: Job? = null
    fun startServer(gameSession: GameSession? = null) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            val ipAddress = connectionManager.getLocalIpAddress() ?: throw Exception("Cannot get local IP address")
//            launch { serverManager.observeMessages() }
//            if (serverJob?.isActive != true) {
//                serverJob = launch { serverManager.startServer(ipAddress, PORT) }
//            }
//            launch {
//                delay(1000)
//                serverManager.observeGameSession()
//            }
            launch {
                delay(1000)
                deviceIpId = ipAddress.substringAfterLast(".")
                serverManager.createGame(deviceIpId, ipAddress, gameSession)
                connectPlayer(ipAddress)
            }
        }
    }

    fun getUserId(): String = deviceIpId
}
