package pl.msiwak.connection

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import pl.msiwak.connection.Json.json
import pl.msiwak.connection.exception.LocalIpNotFoundException
import pl.msiwak.connection.model.ClientActions
import pl.msiwak.connection.model.ServerActions
import pl.msiwak.connection.model.WebSocketEvent
import pl.msiwak.connection.model.WifiState
import kotlin.reflect.KClass

private const val PORT = 63287

class MyConnectionImpl(
    private val ktorClient: KtorClient,
    private val ktorServer: KtorServer,
    private val electionService: ElectionService,
    private val connectionManager: ConnectionManager
) : MyConnection {
    private var electedHostIp: String? = null
    private var job: Job? = null
    private var serverJob: Job? = null

    private lateinit var deviceId: String

    private val _isWifiConnected = MutableStateFlow(false)
    override val isWifiConnected = _isWifiConnected.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading = _isLoading.asStateFlow()

    private var currentHostIp: String? = null

    override val serverMessages: Flow<WebSocketEvent> = ktorServer.messages
        .map(::mapMessage)
        .onEach(::handleWebSocketEvent)

    override val clientMessages: SharedFlow<WebSocketEvent> = ktorClient.webSocketEvent

    init {
        CoroutineScope(Dispatchers.IO).launch {
            launch { electionService.startElection() }
            launch { observeElectionHostIp() }
            launch { ktorClient.isConnected.collectLatest { _isLoading.emit(!it) } }
        }
    }

    override suspend fun connect() {
        currentHostIp?.let { ktorClient.connect(host = it, port = PORT, id = deviceId) } ?: throw Exception("Cannot connect to server")
    }

    override suspend fun disconnectUsers() {
        ktorServer.closeAllSockets()
    }

    override suspend fun send(id: String, webSocketEvent: WebSocketEvent) {
        ktorServer.sendMessage(id, json.encodeToString(webSocketEvent))
    }

    override suspend fun sendToAll(webSocketEvent: WebSocketEvent) {
        ktorServer.sendMessageToAll(json.encodeToString(webSocketEvent))
    }

    override suspend fun sendFromClient(webSocketEvent: WebSocketEvent) {
        ktorClient.send(webSocketEvent)
    }

    suspend fun observeElectionHostIp() {
        val localIP = getDeviceIp()
        val id = getDeviceIp().substringAfterLast(".")

        connectionManager.observeWifiState()
            .onEach { _isWifiConnected.emit(it.name == WifiState.CONNECTED.name) }
            .combine(
                electionService.hostIp.distinctUntilChanged(),
                { wifiState, hostIp -> wifiState to hostIp }
            )
            .filter { (wifiState, _) -> wifiState.name == WifiState.CONNECTED.name }
            .map { (_, hostIp) -> hostIp }
            .collectLatest { hostIp ->
                currentHostIp = hostIp
                println("Observed new host IP: $hostIp")
                electedHostIp = hostIp
                if (hostIp == localIP) {
                    if (!ktorServer.isRunning()) {
                        startServer()
                    }
                }
                if (hostIp.isNotBlank()) {
                    delay(1000)
                    ktorClient.connect(host = hostIp, port = PORT, id = id)
                }
            }
    }

    fun startServer() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
//            launch { observeMessages() }
            val ipAddress = connectionManager.getLocalIpAddress() ?: throw Exception("Cannot get local IP address")
            if (serverJob?.isActive != true) {
                serverJob = launch { ktorServer.startServer(ipAddress, PORT) }
            }
//            launch {
//                delay(1000)
//                serverManager.observeGameSession()
//            }
//            launch {
//                delay(1000)
//                deviceIpId = ipAddress.substringAfterLast(".")
//                serverManager.createGame(deviceIpId, ipAddress, gameSession)
//                connectPlayer(ipAddress)
//            }
        }
    }
//
//    fun connectPlayer(electedIp: String) = CoroutineScope(Dispatchers.IO).launch {
//        deviceIpId = getDeviceIp().substringAfterLast(".")
//        connectPlayerToGame(electedIp)
//    }

    suspend fun getDeviceIp(): String {
        repeat(10) {
            connectionManager.getLocalIpAddress()?.let {
                deviceId = it.substringAfterLast(".")
                return it
            }
            delay(1000)
        }
        return connectionManager.getLocalIpAddress() ?: throw LocalIpNotFoundException()
    }

    override fun getDeviceId(): String = deviceId

    override fun isServerRunning(): Boolean = ktorServer.isRunning()

    override fun setCustomEvents(events: List<Pair<KClass<out WebSocketEvent>, KSerializer<out WebSocketEvent>>>) {
        Json.generateJson(events)
    }

    private fun mapMessage(message: String): WebSocketEvent {
        return when {
            message.startsWith("Client disconnected: ") -> ClientActions.UserDisconnected(message.substringAfter("Client disconnected: "))
            message.startsWith("Server started") -> ServerActions.ServerStarted
            else -> json.decodeFromString<WebSocketEvent>(message)
        }
    }

    private suspend fun handleWebSocketEvent(webSocketEvent: WebSocketEvent) {
        when (webSocketEvent) {
            is ClientActions.UserDisconnected -> ktorServer.closeSocket(webSocketEvent.id)
            is ClientActions.ServerDownDetected -> _isLoading.value = true
            else -> Unit
        }
    }

//    private suspend fun connectPlayerToGame(ip: String) {
//        val player = Player(id = deviceIpId, isActive = true)
//        ktorClient.connect(host = ip, port = PORT, player = player)
//    }


//    suspend fun observeMessages() {
//        ktorServer.messages.map {
//            if (it.startsWith("Client disconnected: ")) {
//                WebSocketEvent.ClientActions.UserDisconnected(it.substringAfter("Client disconnected: "))
//            } else {
//                json.decodeFromString<WebSocketEvent>(it)
//            }
//        }
//            .collectLatest { event ->
//                when (event) {
//                    is WebSocketEvent.ClientActions.UserConnected -> gameManager.joinGame(event.player)
//
//                    is WebSocketEvent.ClientActions.UserDisconnected -> {
////                        gameManager.leaveGame(event.id)
//                        gameManager.disablePlayer(event.id)
//                        ktorServer.closeSocker(event.id)
//                    }
//
//                    is WebSocketEvent.ServerActions.UpdateGameSession -> {
//                        gameManager.getGameSession()?.let {
//                            ktorServer.sendMessageToAll(
//                                json.encodeToString<WebSocketEvent>(event)
//                            )
//                        }
//                    }
//
//                    is WebSocketEvent.ClientActions.SetPlayerReady -> {
//                        gameManager.setPlayerReady(event.id)
//                    }
//
//                    is WebSocketEvent.ClientActions.AddCard -> {
//                        gameManager.addCardToGame(event.id, event.cardText)
//                    }
//
//                    is WebSocketEvent.ClientActions.ContinueGame -> {
//                        gameManager.continueGame()
//                    }
//
//                    is WebSocketEvent.ClientActions.JoinTeam -> {
//                        gameManager.joinTeam(event.id, event.teamName)
//                    }
//
//                    is WebSocketEvent.ClientActions.SetCorrectAnswer -> {
//                        gameManager.setCorrectAnswer(event.cardText)
//                    }
//
//                    is WebSocketEvent.ClientActions.AddPlayerName -> {
//                        gameManager.addPlayerName(event.id, event.name)
//                    }
//
//                    else -> Unit
//                }
//            }
}
