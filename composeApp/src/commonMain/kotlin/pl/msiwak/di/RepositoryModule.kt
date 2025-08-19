package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.data.player.PlayerRepository
import pl.msiwak.data.game.GameRepository

internal val repositoryModule = module {
    single { PlayerRepository(get()) }
    single { GameRepository(get(), get()) }
}
