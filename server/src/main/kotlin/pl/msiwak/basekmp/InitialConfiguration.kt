package pl.msiwak.basekmp

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import pl.msiwak.basekmp.database.DatabaseFactory
import pl.msiwak.basekmp.di.databaseModule
import pl.msiwak.basekmp.di.domainModule
import pl.msiwak.basekmp.di.repositoryModule
import pl.msiwak.basekmp.routing.configureRouting

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
