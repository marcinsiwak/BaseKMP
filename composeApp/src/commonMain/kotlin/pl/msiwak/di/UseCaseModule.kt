package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.domain.game.AddPlayerToGameUseCase
import pl.msiwak.domain.game.DisconnectUseCase
import pl.msiwak.domain.game.FindGameIPAddressUseCase
import pl.msiwak.domain.game.ObservePlayersConnectionUseCase
import pl.msiwak.domain.game.RemovePlayerFromGameUseCase
import pl.msiwak.domain.game.StartGameUseCase
import pl.msiwak.domain.game.StopGameUseCase
import pl.msiwak.domainimpl.game.AddPlayerToGameUseCaseImpl
import pl.msiwak.domainimpl.game.DisconnectUseCaseImpl
import pl.msiwak.domainimpl.game.FindGameIPAddressUseCaseImpl
import pl.msiwak.domainimpl.game.ObservePlayersConnectionUseCaseImpl
import pl.msiwak.domainimpl.game.RemovePlayerFromGameUseCaseImpl
import pl.msiwak.domainimpl.game.StartGameUseCaseImpl
import pl.msiwak.domainimpl.game.StopGameUseCaseImpl

internal val useCaseModule = module {
    single<StartGameUseCase> { StartGameUseCaseImpl(get()) }
    single<StopGameUseCase> { StopGameUseCaseImpl(get()) }
    single<AddPlayerToGameUseCase> { AddPlayerToGameUseCaseImpl(get()) }
    single<RemovePlayerFromGameUseCase> { RemovePlayerFromGameUseCaseImpl(get()) }
    single<ObservePlayersConnectionUseCase> { ObservePlayersConnectionUseCaseImpl(get()) }
    single<FindGameIPAddressUseCase> { FindGameIPAddressUseCaseImpl(get()) }
    single<DisconnectUseCase> { DisconnectUseCaseImpl(get()) }
}
