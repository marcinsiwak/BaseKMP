package pl.msiwak.network

import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

actual class KtorServer {
    private var server: EmbeddedServer<*,*>? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    actual fun startServer() {
        scope.launch {
            server = embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
                configureServer()
            }.start(wait = false)

            println("Server started at http://8080:0.0.0.0")
        }
    }

    actual fun stopServer() {
        server?.stop(1000, 2000)
        server = null
        println("Server stopped")
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

        // Configure routing
        routing {
            get("/") {
                call.respondText("Cards Game Server is running!")
            }

            route("/api") {
                get("/health") {
                    call.respondText("Server is healthy")
                }

                route("/players") {
                    get {
                        call.respondText("Get all players")
                    }

                    post {
                        call.respondText("Create new player")
                    }
                }

                route("/game") {
                    get {
                        call.respondText("Get game state")
                    }

                    post("/start") {
                        call.respondText("Start new game")
                    }

                    post("/join") {
                        call.respondText("Join game")
                    }
                }
            }
        }
    }

    fun isRunning(): Boolean = server != null

    fun getServerUrl(port: Int = 8080): String = "http://localhost:$port"
}
