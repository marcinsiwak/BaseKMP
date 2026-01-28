package pl.msiwak.connection

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import pl.msiwak.connection.Json.json
import pl.msiwak.connection.engine.EngineProvider
import pl.msiwak.connection.model.ClientActions
import pl.msiwak.connection.model.WebSocketEvent
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class KtorClient(engine: EngineProvider) {
    private var scope = CoroutineScope(Dispatchers.Main)

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    private val _webSocketEvent = MutableSharedFlow<WebSocketEvent>()
    val webSocketEvent: SharedFlow<WebSocketEvent> = _webSocketEvent.asSharedFlow()
    private val _webSocketClientEvent = MutableSharedFlow<WebSocketEvent>()
    private val webSocketClientEvent: SharedFlow<WebSocketEvent> = _webSocketClientEvent.asSharedFlow()

    private val client = HttpClient(engine.getEngine()) {
        install(WebSockets) {
            pingInterval = 15.seconds
        }
    }

    suspend fun connect(host: String, port: Int, id: String) {
        println("OUTPUT: KtorClient connecting to $host:$port")
        runCatching {
            client.webSocket(
                method = HttpMethod.Get,
                host = host,
                port = port,
                path = "/ws?id=$id"
            ) {
                _isConnected.value = true
                send(
                    json.encodeToString<WebSocketEvent>(
                        ClientActions.UserConnected(
                            id,
                            host.substringAfterLast(".") == id
                        )
                    )
                )
                launch {
                    webSocketClientEvent.collect { message ->
                        send(json.encodeToString<WebSocketEvent>(message))
                    }
                }
                listenForResponse()
            }
        }.onFailure {
            _isConnected.value = false
            println("OUTPUT: KtorClient connect failed: ${it.message}")
            if (it.message?.contains("-1009") == true || it.message?.contains("-1004") == true || it.message == "Connection refused") {
                delay(1000)
                connect(host, port, id)
            }

        }
    }

    fun send(webSocketEvent: WebSocketEvent) {
        scope.launch {
            _webSocketClientEvent.emit(webSocketEvent)
        }
    }

    private suspend fun DefaultClientWebSocketSession.listenForResponse() {
        runCatching {
            while (coroutineContext.isActive) {
                select {
                    incoming.onReceive { frame ->
                        when (frame) {
                            is Frame.Text -> {
                                val text = frame.readText()
                                println("OUTPUT: Received text: $text")
                                val event = json.decodeFromString<WebSocketEvent>(text)
                                _webSocketEvent.emit(event)
                            }

                            else -> println("OUTPUT: Received non-text frame: $frame")
                        }
                    }
                }
            }
        }.onFailure {
            _isConnected.value = false
            when (it) {
                is ClosedReceiveChannelException -> {
                    when (val reason = closeReason.await()?.knownReason) {
                        CloseReason.Codes.GOING_AWAY, CloseReason.Codes.NORMAL -> {
                            println("OUTPUT: Connection closed normally player disconnected")
                        }

                        else -> {
                            println("OUTPUT: Connection closed with reason: $reason")
                            _webSocketEvent.emit(ClientActions.ServerDownDetected)
                        }
                    }
                }

                is CancellationException -> {
                    println("OUTPUT: listenForResponse cancelled: ${it.message}")
                    withContext(NonCancellable) {
                        _webSocketEvent.emit(ClientActions.ServerDownDetected)
                    }
                }

                else -> println("OUTPUT: Error in listenForResponse: ${it.message}")
            }
        }
    }

    suspend fun disconnect(playerId: String) {
        val event: WebSocketEvent = ClientActions.UserDisconnected(playerId)
        _webSocketClientEvent.emit(event)
    }
}