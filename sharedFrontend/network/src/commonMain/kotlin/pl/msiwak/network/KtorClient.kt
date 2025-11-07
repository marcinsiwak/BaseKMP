package pl.msiwak.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import pl.msiwak.common.model.Player
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.network.engine.EngineProvider

class KtorClient(engine: EngineProvider) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    private var scope = CoroutineScope(Dispatchers.Main)

    private val _webSocketEvent = MutableSharedFlow<WebSocketEvent>()
    val webSocketEvent: SharedFlow<WebSocketEvent> = _webSocketEvent.asSharedFlow()

    private val _webSocketClientEvent = MutableSharedFlow<WebSocketEvent>()
    private val webSocketClientEvent: SharedFlow<WebSocketEvent> = _webSocketClientEvent.asSharedFlow()

    val client = HttpClient(engine.getEngine()) {
        install(WebSockets)
    }

    suspend fun connect(host: String, port: Int, player: Player) {
        client.webSocket(
            method = HttpMethod.Get,
            host = host,
            port = port,
            path = "/ws?id=${player.id}"
        ) {
            send(json.encodeToString<WebSocketEvent>(WebSocketEvent.ClientActions.PlayerConnected(player)))
            launch {
                webSocketClientEvent.collect { message ->
                    when (message) {
                        is WebSocketEvent.ClientActions -> send(json.encodeToString<WebSocketEvent>(message))

                        else -> Unit
                    }
                }
            }
            listenForResponse()
        }
    }

    fun send(webSocketEvent: WebSocketEvent) {
        scope.launch {
            _webSocketClientEvent.emit(webSocketEvent)
        }
    }

    private suspend fun DefaultClientWebSocketSession.listenForResponse() {
        runCatching {
            while (true) {
                when (val frame = incoming.receive()) {
                    is Frame.Text -> {
                        val text = frame.readText()
                        println("OUTPUT: Received text: $text")
                        val event = json.decodeFromString<WebSocketEvent>(text)
                        _webSocketEvent.emit(event)
                    }

                    else -> println("OUTPUT: Received non-text frame: $frame")
                }
            }
        }.onFailure {
            when (it) {
                is ClosedReceiveChannelException -> {
                    when (val reason = closeReason.await()?.knownReason) {
                        CloseReason.Codes.GOING_AWAY, CloseReason.Codes.NORMAL -> {
                            println("OUTPUT: Connection closed normally player disconnected")
                        }

                        else -> {
                            println("OUTPUT: Connection closed with reason: $reason")
                            _webSocketEvent.emit(WebSocketEvent.ClientActions.ServerDownDetected)
                        }
                    }
                }

                is CancellationException -> {
                    println("OUTPUT: listenForResponse cancelled: ${it.message}")
                    withContext(NonCancellable) {
                        _webSocketEvent.emit(WebSocketEvent.ClientActions.ServerDownDetected)
                    }
                }

                else -> println("OUTPUT: Error in listenForResponse: ${it.message}")
            }
        }
    }

    suspend fun disconnect(playerId: String) {
        val event: WebSocketEvent = WebSocketEvent.ClientActions.PlayerClientDisconnected(playerId)
        _webSocketClientEvent.emit(event)
    }
}