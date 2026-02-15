package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.network.KtorClient
import pl.msiwak.network.engine.EngineProvider

internal val networkModule = module {
    single { EngineProvider() }
    single { KtorClient(get()) }
}
