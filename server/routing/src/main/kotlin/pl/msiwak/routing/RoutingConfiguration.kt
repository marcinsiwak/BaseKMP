package pl.msiwak.routing

import io.ktor.server.application.Application
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject
import pl.msiwak.domain.UseCase

fun Application.configureRouting() {
    routing {
        addRoutes()
    }
}


private fun Route.addRoutes() {
    // create separate routes for scope of operations
    val useCase: UseCase by inject()

    post("/example") {
        with(call) {
            useCase()
            // inject use case here
        }
    }
}
