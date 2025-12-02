package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.gamemanager.GameManager
import pl.msiwak.gamemanager.GameManagerImpl
import pl.msiwak.network.KtorClient
import pl.msiwak.network.ServerManager
import pl.msiwak.network.engine.EngineProvider
import pl.msiwak.network.service.ElectionService
import pl.msiwak.network.service.GameService

internal val networkModule = module {
    single { EngineProvider() }
    single { KtorClient(get()) }
    single { GameService(get(), get(), get()) }
    single { ElectionService(get(), get()) }
    single { ServerManager(get(), get()) }
    single<GameManager> { GameManagerImpl(get()) }
}
