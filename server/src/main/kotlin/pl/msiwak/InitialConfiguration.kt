package pl.msiwak

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import pl.msiwak.database.DatabaseFactory
import pl.msiwak.di.databaseModule
import pl.msiwak.di.domainModule
import pl.msiwak.di.networkModule
import pl.msiwak.di.repositoryModule
import pl.msiwak.routing.configureRouting

fun Application.initialConfiguration() {
//    DatabaseFactory.init(url = "jdbc:postgresql://localhost:5432/test", user = "postgres", password = "password")
    configureRouting()

    install(Koin) {
        slf4jLogger()
        modules(
            databaseModule,
            domainModule,
            repositoryModule,
            networkModule
        )
    }

    install(ContentNegotiation) {
        json(
            json = Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
    }
}
