package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.domain.player.GetPlayersUseCase
import pl.msiwak.domainimpl.player.GetPlayersUseCaseImpl

internal val useCaseModule = module {
    single<GetPlayersUseCase> { GetPlayersUseCaseImpl(get()) }
}
