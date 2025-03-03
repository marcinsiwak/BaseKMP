package pl.msiwak.basekmp.routing

import io.ktor.server.application.Application
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        addRoutes()
    }
}

private fun Route.addRoutes() {
    post("/example") {
        with(call) {
            // inject use case here
        }
    }
}
