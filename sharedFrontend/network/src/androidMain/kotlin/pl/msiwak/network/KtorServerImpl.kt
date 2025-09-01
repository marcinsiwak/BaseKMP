package pl.msiwak.network

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class KtorServerImpl : KtorServer {
    private var server: EmbeddedServer<*, *>? = null
    private val _messages = MutableSharedFlow<String>()
    override val messages: Flow<String> = _messages.asSharedFlow()

    private var activeSessions = mutableMapOf<String, WebSocketSession>()

    override fun startServer(host: String, port: Int) {
        server = embeddedServer(CIO, port = port, host = host) {
            configureServer()
        }.start(wait = false)

    }

    override fun stopServer() {
        server?.stop(1000, 2000)
        server = null
        println("OUTPUT: Server stopped")
    }

    private fun Application.configureServer() {
        install(WebSockets)

        routing {
            webSocket("/ws") {
                val userId = call.request.queryParameters["id"] ?: "0.0.0.0"

                activeSessions[userId]?.close(
                    CloseReason(CloseReason.Codes.NORMAL, "Another session opened")
                ) ?: _messages.emit("Player connected: $userId")

                activeSessions[userId] = this

                runCatching {
                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            val receivedText = frame.readText()
                            _messages.emit(receivedText)
                        }
                    }
                }.onFailure { exception ->
                    println("OUTPUT: WebSocket exception: ${exception.localizedMessage}")
                }
            }
        }
    }

    override suspend fun sendMessage(userId: String, message: String) {
        activeSessions[userId]?.send(message)
    }

    override suspend fun sendMessageToAll(message: String) {
        activeSessions.values.forEach { it.send(message) }
    }

    override suspend fun closeSocker(userId: String) {
        activeSessions[userId]?.close(CloseReason(CloseReason.Codes.NORMAL, "Closed by server"))
        activeSessions.remove(userId)
    }

    fun isRunning(): Boolean = server != null

    fun getServerUrl(port: Int = 8080): String = "http://localhost:$port"
}
