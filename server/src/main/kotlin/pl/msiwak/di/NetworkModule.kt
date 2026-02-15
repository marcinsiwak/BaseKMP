package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.network.player.PlayersService

val networkModule = module {
    single { PlayersService() }
}
