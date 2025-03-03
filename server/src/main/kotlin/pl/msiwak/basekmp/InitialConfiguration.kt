package pl.msiwak.basekmp

import io.ktor.server.application.Application
import pl.msiwak.basekmp.database.DatabaseFactory
import pl.msiwak.basekmp.routing.configureRouting

fun Application.initialConfiguration() {
    DatabaseFactory.init(url = "", user = "", password = "")
    configureRouting()
}
