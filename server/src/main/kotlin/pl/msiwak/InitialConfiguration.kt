package pl.msiwak

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import pl.msiwak.database.DatabaseFactory
import pl.msiwak.di.databaseModule
import pl.msiwak.di.domainModule
import pl.msiwak.di.repositoryModule
import pl.msiwak.routing.configureRouting

fun Application.initialConfiguration() {
    DatabaseFactory.init(url = "", user = "", password = "")
    configureRouting()

    install(Koin) {
//        slf4jLogger()
        modules(
            databaseModule,
            domainModule,
            repositoryModule
        )
    }
}
