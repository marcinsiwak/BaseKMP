package pl.msiwak.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.network.engine.EngineProvider

class KtorClient(engine: EngineProvider) {

    private var session: WebSocketSession? = null
    private val json = Json { ignoreUnknownKeys = true }

    private val scope = CoroutineScope(Dispatchers.Main)

    private val _webSocketEvents = MutableSharedFlow<WebSocketEvent>()
    fun observeWebSocketEvents(): SharedFlow<WebSocketEvent> = _webSocketEvents.asSharedFlow()

    val client = HttpClient(engine.getEngine()) {
        install(WebSockets) {
            pingIntervalMillis = 20_000
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    fun connect(host: String, port: Int, playerName: String) {
        scope.launch {
            runCatching {
                client.webSocket(
                    method = HttpMethod.Get,
                    host = host,
                    port = port,
                    path = "/ws?name=$playerName"
                ) {
                    session = this
                    listenForResponse()
                }
            }.onFailure { exception ->
                println("OUTPUT: Failed to connect: $exception")
            }
        }
    }

    private suspend fun WebSocketSession.listenForResponse() {
        incoming.consumeEach { frame ->
            when (frame) {
                is Frame.Text -> {
                    val text = frame.readText()
                    val event = json.decodeFromString<WebSocketEvent.PlayerConnection.PlayerConnected>(text)
                    _webSocketEvents.emit(event)
                }

                else -> {}
            }
        }
    }

    fun disconnect() {
        scope.launch {
            session?.close()
            client.close()
        }
    }
}