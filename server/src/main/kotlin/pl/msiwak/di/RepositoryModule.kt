package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.data.player.PlayerRepository

val repositoryModule = module {
    single { PlayerRepository(get()) }
}
