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
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import pl.msiwak.common.model.WebSocketEvent

actual class KtorServer {
    private var server: EmbeddedServer<*, *>? = null
    private val json = Json { ignoreUnknownKeys = true }
    private val scope = CoroutineScope(Dispatchers.IO)

    private val playersList = mutableListOf<String>()

    actual fun startServer() {
        scope.launch {
            server = embeddedServer(CIO, port = 8080, host = "192.168.0.62") {
                configureServer()
            }.start(wait = false)
        }
    }

    actual fun stopServer() {
        server?.stop(1000, 2000)
        server = null
        println("OUTPUT: Server stopped")
    }

    val messageResponseFlow = MutableSharedFlow<WebSocketEvent>()
    val sharedFlow = messageResponseFlow.asSharedFlow()

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
                val job = launch {
                    sharedFlow.collect { message ->
                        val event = WebSocketEvent.PlayerConnection.PlayerConnected(playersList)
                        send(json.encodeToString(WebSocketEvent.PlayerConnection.PlayerConnected.serializer(), event))
                    }
                }

                val playerName = call.request.queryParameters["name"] ?: "Guest"

                playersList.add(playerName)
                messageResponseFlow.emit(WebSocketEvent.PlayerConnection.PlayerConnected(playersList))

                runCatching {
                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            val receivedText = frame.readText()
//                            val messageResponse = MessageResponse(receivedText)
//                            messageResponseFlow.emit(messageResponse)
                        }
                    }
                }.onFailure { exception ->
                    println("WebSocket exception: ${exception.localizedMessage}")
                }.also {
                    job.cancel()
                }

            }
        }
    }

    fun isRunning(): Boolean = server != null

    fun getServerUrl(port: Int = 8080): String = "http://localhost:$port"
}
