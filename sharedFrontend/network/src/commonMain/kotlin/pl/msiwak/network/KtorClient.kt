package pl.msiwak.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import pl.msiwak.common.model.Player
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.network.engine.EngineProvider

class KtorClient(engine: EngineProvider) {

    private var session: WebSocketSession? = null
    private val json = Json {
        ignoreUnknownKeys = true
        serializersModule = SerializersModule {
            polymorphic(WebSocketEvent::class) {
                subclass(WebSocketEvent.PlayerConnected::class, WebSocketEvent.PlayerConnected.serializer())
                subclass(WebSocketEvent.PlayerDisconnected::class, WebSocketEvent.PlayerDisconnected.serializer())
                subclass(WebSocketEvent.PlayerClientDisconnected::class, WebSocketEvent.PlayerClientDisconnected.serializer())
            }
        }
    }

    private var scope = CoroutineScope(Dispatchers.Main)

    private val _webSocketEvent = MutableSharedFlow<WebSocketEvent>()
    val webSocketEvent: SharedFlow<WebSocketEvent> = _webSocketEvent.asSharedFlow()

    private val _webSocketClientEvent = MutableSharedFlow<WebSocketEvent>()
    private val webSocketClientEvent: SharedFlow<WebSocketEvent> = _webSocketClientEvent.asSharedFlow()

    val client = HttpClient(engine.getEngine()) {
        install(WebSockets) {
            pingIntervalMillis = 20_000
            contentConverter = KotlinxWebsocketSerializationConverter(json)
        }
    }

    suspend fun connect(host: String, port: Int, player: Player) {
        if (!scope.isActive) {
            scope = CoroutineScope(Dispatchers.Main)
        }
        scope.launch {
            runCatching {
                client.webSocket(
                    method = HttpMethod.Get,
                    host = host,
                    port = port,
                    path = "/ws?id=${player.id}"
                ) {
                    launch {
                        webSocketClientEvent.collect { message ->
                            when (message) {
                                is WebSocketEvent.PlayerClientDisconnected -> {
                                    send(
                                        json.encodeToString(
                                            WebSocketEvent.PlayerClientDisconnected.serializer(),
                                            message
                                        )
                                    )
                                }

                                else -> Unit
                            }

                        }
                    }
                    session = this
                    listenForResponse()
                }
            }.onFailure { exception ->
                println("OUTPUT: Failed to connect: $exception")
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.listenForResponse() {
        incoming.consumeEach { frame ->
            when (frame) {
                is Frame.Text -> {
                    val text = frame.readText()
                    print("OUTPUT: Received text: $text")
                    val event = json.decodeFromString(WebSocketEvent.PlayerConnected.serializer(), text)
                    _webSocketEvent.emit(event)
                }

                else -> {}
            }
        }
    }

    suspend fun disconnect(playerId: String) {
        _webSocketClientEvent.emit(WebSocketEvent.PlayerClientDisconnected(playerId))
//        scope.cancel()
        session = null
    }
}