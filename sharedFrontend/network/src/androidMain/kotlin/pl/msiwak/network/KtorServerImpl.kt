package pl.msiwak.network

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import pl.msiwak.common.model.WebSocketEvent

class KtorServerImpl : KtorServer {
    private var server: EmbeddedServer<*, *>? = null
    private val json = Json { ignoreUnknownKeys = true }
    private var scope = CoroutineScope(Dispatchers.IO)

    private val _messageResponseFlow = MutableSharedFlow<WebSocketEvent>()
    private val messageResponseFlow = _messageResponseFlow.asSharedFlow()

    private var activeSessions = mutableMapOf<String, WebSocketSession>()

    override fun startServer(host: String, port: Int) {
        if (!scope.isActive) {
            scope = CoroutineScope(Dispatchers.IO)
        }
        scope.launch {
            server = embeddedServer(CIO, port = port, host = host) {
                configureServer()
            }.start(wait = false)
        }
    }

    override fun stopServer() {
        scope.cancel()
        server?.stop(1000, 2000)
        server = null
        println("OUTPUT: Server stopped")
    }

    private fun Application.configureServer() {
        install(ContentNegotiation) {
            json(
                json = Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }

        install(WebSockets)

        routing {
            webSocket("/ws") {
                val userId = call.request.queryParameters["id"] ?: "0.0.0.0"

                activeSessions[userId]?.close(
                    CloseReason(CloseReason.Codes.NORMAL, "Another session opened")
                )

                activeSessions[userId] = this

                val job = launch {
                    messageResponseFlow.collect { message ->
                        when (message) {
                            is WebSocketEvent.ServerEvents.PlayerConnected -> send(
                                json.encodeToString(
                                    WebSocketEvent.ServerEvents.PlayerConnected.serializer(),
                                    message
                                )
                            )

                            is WebSocketEvent.ClientEvents.PlayerDisconnected -> {
                                activeSessions.remove(message.player)
                                send(
                                    json.encodeToString(
                                        WebSocketEvent.ServerEvents.PlayerDisconnected.serializer(),
                                        WebSocketEvent.ServerEvents.PlayerDisconnected(activeSessions.keys.toList())
                                    )
                                )
                                activeSessions[message.player]?.close()
                            }

                            else -> Unit
                        }

                    }
                }

                _messageResponseFlow.emit(WebSocketEvent.ServerEvents.PlayerConnected(activeSessions.keys.toList()))

                runCatching {
                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            val receivedText = frame.readText()
                            val event =
                                json.decodeFromString<WebSocketEvent.ClientEvents.PlayerDisconnected>(receivedText)
                            _messageResponseFlow.emit(event)
                        }
                    }
                }.onFailure { exception ->
                    println("OUTPUT: WebSocket exception: ${exception.localizedMessage}")
                }.also {
                    job.cancel()
                }

            }
        }
    }

    fun isRunning(): Boolean = server != null

    fun getServerUrl(port: Int = 8080): String = "http://localhost:$port"
}
