package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.domain.player.GetPlayersUseCase
import pl.msiwak.domain.game.StartGameUseCase
import pl.msiwak.domain.game.StopGameUseCase
import pl.msiwak.domainimpl.player.GetPlayersUseCaseImpl
import pl.msiwak.domainimpl.game.StartGameUseCaseImpl
import pl.msiwak.domainimpl.game.StopGameUseCaseImpl

internal val useCaseModule = module {
    single<GetPlayersUseCase> { GetPlayersUseCaseImpl(get()) }
    single<StartGameUseCase> { StartGameUseCaseImpl(get()) }
    single<StopGameUseCase> { StopGameUseCaseImpl(get()) }
}
