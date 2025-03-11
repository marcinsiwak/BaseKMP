package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.data.player.PlayerRepository

internal val repositoryModule = module {
    single { PlayerRepository(get()) }
}
