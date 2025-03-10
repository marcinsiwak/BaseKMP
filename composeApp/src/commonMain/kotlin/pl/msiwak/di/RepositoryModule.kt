package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.data.player.PlayerRepository
import pl.msiwak.domain.player.GetPlayersUseCase
import pl.msiwak.domainimpl.player.GetPlayersUseCaseImpl

internal val repositoryModule = module {
    single { PlayerRepository(get()) }
}