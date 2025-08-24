package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.network.ConnectionManager
import pl.msiwak.network.KtorClient
import pl.msiwak.network.engine.EngineProvider
import pl.msiwak.network.service.GameService

internal val networkModule = module {
    single { EngineProvider() }
    single { KtorClient(get()) }
    single { GameService(get(), get(), get()) }
    single { ConnectionManager() }
}
