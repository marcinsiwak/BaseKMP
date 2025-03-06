package pl.msiwak

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import pl.msiwak.Greeting
import pl.msiwak.SERVER_PORT

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    initialConfiguration()

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
    }
}