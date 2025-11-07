package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.data.game.GameRepository

internal val repositoryModule = module {
    single { GameRepository(get(), get(), get()) }
}
