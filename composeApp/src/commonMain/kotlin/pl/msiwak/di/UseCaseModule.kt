package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.domain.player.GetPlayersUseCase
import pl.msiwak.domain.session.StartSessionUseCase
import pl.msiwak.domain.session.StopSessionUseCase
import pl.msiwak.domainimpl.player.GetPlayersUseCaseImpl
import pl.msiwak.domainimpl.session.StartSessionUseCaseImpl
import pl.msiwak.domainimpl.session.StopSessionUseCaseImpl

internal val useCaseModule = module {
    single<GetPlayersUseCase> { GetPlayersUseCaseImpl(get()) }
    single<StartSessionUseCase> { StartSessionUseCaseImpl(get()) }
    single<StopSessionUseCase> { StopSessionUseCaseImpl(get()) }
}
