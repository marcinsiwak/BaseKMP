package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.data.player.PlayerRepository
import pl.msiwak.data.session.SessionRepository

internal val repositoryModule = module {
    single { PlayerRepository(get()) }
    single { SessionRepository(get()) }
}
