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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import pl.msiwak.common.model.GameActions
import pl.msiwak.common.model.Player
import pl.msiwak.connection.model.ClientActions
import pl.msiwak.connection.model.WebSocketEvent
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
        runCatching {
            client.webSocket(
                method = HttpMethod.Get,
                host = host,
                port = port,
                path = "/ws?id=${player.id}"
            ) {
//                send(json.encodeToString<WebSocketEvent>(ClientActions.UserConnected(player.id)))
                launch {
                    webSocketClientEvent.collect { message ->
                        when (message) {
                            is GameActions -> send(json.encodeToString<WebSocketEvent>(message))

                            else -> Unit
                        }
                    }
                }
                listenForResponse()
            }
        }.onFailure {
            println("OUTPUT: KtorClient connect failed: ${it.message}")
            if (it.message?.contains("-1009") == true) {
                delay(1000)
                connect(host, port, player)
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
            while (isActive) {
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