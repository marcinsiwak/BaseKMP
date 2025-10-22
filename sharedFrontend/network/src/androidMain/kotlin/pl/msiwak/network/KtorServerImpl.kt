package pl.msiwak.network

import android.util.Log
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class KtorServerImpl : KtorServer {
    private var server: EmbeddedServer<*, *>? = null
    private val _messages = MutableSharedFlow<String>()
    override val messages: Flow<String> = _messages.asSharedFlow()

    private var activeSessions = mutableMapOf<String, WebSocketSession>()

    private val mutex = Mutex()

    override fun startServer(host: String, port: Int) {
        if (server != null) return
        server = embeddedServer(CIO, port = port, host = host) {

            configureServer()
        }.start(wait = true)
    }

    override suspend fun stopServer() {
        server?.stopSuspend(1000, 2000)
        server = null
        println("OUTPUT: Server stopped")
    }

    private fun Application.configureServer() {
        install(WebSockets) {
            pingPeriod = 15.seconds
            timeout = 30.seconds
        }

        routing {
            webSocket("/ws") {
                val userId = call.request.queryParameters["id"] ?: "0.0.0.0"

                activeSessions[userId]?.close(
                    CloseReason(CloseReason.Codes.NORMAL, "Another session opened")
                )

                activeSessions[userId] = this

                runCatching {
                    while (true) {
                        when (val frame = incoming.receive()) {
                            is Frame.Text -> {
                                val receivedText = frame.readText()
                                println("OUTPUT: KtorServerImpl Received text: $receivedText")
                                _messages.emit(receivedText)
                            }

                            else -> println("OUTPUT: Received non-text frame: $frame")
                        }
                    }
                }.onFailure { exception ->
                    Log.e("OUTPUT", "OUTPUT - KtorServerImpl: $exception")
                    withContext(NonCancellable) {
                        activeSessions[userId]?.close(
                            CloseReason(
                                CloseReason.Codes.NORMAL,
                                "Another session opened"
                            )
                        )
                        _messages.emit("Client disconnected: $userId")
                        this@withContext.coroutineContext.cancelChildren()
                    }
                }
            }
        }
    }

    override suspend fun sendMessage(userId: String, message: String) {
        mutex.withLock {
            activeSessions[userId]?.send(message)
        }
    }

    override suspend fun sendMessageToAll(message: String) {
        mutex.withLock {
            activeSessions.values.forEach { it.send(message) }
        }
    }

    override suspend fun closeSocker(userId: String) {
        mutex.withLock {
            activeSessions[userId]?.close(CloseReason(CloseReason.Codes.NORMAL, "Closed by server"))
            activeSessions.remove(userId)
        }
    }

    override suspend fun closeAllSockets() {
        mutex.withLock {
            activeSessions.values.forEach {
                it.close(CloseReason(CloseReason.Codes.NORMAL, "Closed by server"))
            }
            activeSessions.clear()
        }
    }
}
