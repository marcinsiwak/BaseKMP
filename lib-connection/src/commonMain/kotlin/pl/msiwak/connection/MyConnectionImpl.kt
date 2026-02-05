package pl.msiwak.connection

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
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

    private val scope = CoroutineScope(Dispatchers.IO)

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->}

    override val serverMessages: SharedFlow<WebSocketEvent> = ktorServer.messages
        .map(::mapMessage)
//        .onEach(::handleWebSocketEvent)
        .shareIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            replay = 0
        )

    override val clientMessages: SharedFlow<WebSocketEvent> = ktorClient.webSocketEvent
        .onEach(::handleClientWebSocketEvent)
        .shareIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            replay = 0
        )

    init {
        CoroutineScope(Dispatchers.IO).launch {
            launch { electionService.startElection() }
            launch { observeElectionHostIp() }
            launch { ktorClient.isConnected.collectLatest { _isLoading.emit(!it) } }
        }
    }

    override suspend fun connect() {
        currentHostIp?.let { ktorClient.connect(host = it, port = PORT, id = deviceId) }
            ?: throw Exception("Cannot connect to server")
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
            .filter { (wifiState, hostIp) -> wifiState.name == WifiState.CONNECTED.name && hostIp.isNotBlank() }
            .map { (_, hostIp) -> hostIp }
            .collectLatest { hostIp ->
                currentHostIp = hostIp
                println("Observed new host IP: $hostIp")
                electedHostIp = hostIp
                if (hostIp == localIP) {
                    println("is ktorServerRunning : ${ktorServer.isRunning()}")
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
        serverJob?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            val ipAddress = connectionManager.getLocalIpAddress() ?: throw Exception("Cannot get local IP address")
            if (serverJob?.isActive != true) {
                serverJob = launch(errorHandler) { ktorServer.startServer(ipAddress, PORT) }
            }
        }
    }

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

    override fun setHasSession(hasSession: Boolean, lastUpdate: Long) {
        electionService.setHasSession(hasSession, lastUpdate)
    }

    private fun mapMessage(message: String): WebSocketEvent {
        println("Map messages: $message")
        return when {
            message.startsWith("Client disconnected: ") -> ClientActions.UserDisconnected(message.substringAfter("Client disconnected: "))
            message.contains("Server started") -> ServerActions.ServerStarted
            message.contains("Server stopped") -> ClientActions.ServerDownDetected
            else -> json.decodeFromString<WebSocketEvent>(message)
        }
    }

    private suspend fun handleWebSocketEvent(webSocketEvent: WebSocketEvent) {
        when (webSocketEvent) {
            is ClientActions.UserDisconnected -> ktorServer.closeSocket(webSocketEvent.id)
            is ClientActions.ServerDownDetected -> {
                _isLoading.value = true //to remove
            }

            else -> Unit
        }
    }

    private suspend fun handleClientWebSocketEvent(webSocketEvent: WebSocketEvent) {
        when (webSocketEvent) {
            is ClientActions.UserDisconnected -> ktorServer.closeSocket(webSocketEvent.id)
            is ClientActions.ServerDownDetected -> {
                electionService.clearHost()
                ktorServer.stopServer()
                _isLoading.value = true
            }

            else -> Unit
        }
    }
}
