package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.data.game.GameRepository
import pl.msiwak.data.game.ServerManager

internal val repositoryModule = module {
    single { ServerManager(get()) }
    single { GameRepository(get(), get()) }
}
