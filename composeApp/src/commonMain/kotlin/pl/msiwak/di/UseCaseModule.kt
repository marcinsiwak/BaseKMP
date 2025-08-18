package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.domain.player.GetPlayersUseCase
import pl.msiwak.domain.session.StartSessionUseCase
import pl.msiwak.domainimpl.player.GetPlayersUseCaseImpl
import pl.msiwak.domainimpl.session.StartSessionUseCaseImpl

internal val useCaseModule = module {
    single<GetPlayersUseCase> { GetPlayersUseCaseImpl(get()) }
    single<StartSessionUseCase> { StartSessionUseCaseImpl(get()) }
}
