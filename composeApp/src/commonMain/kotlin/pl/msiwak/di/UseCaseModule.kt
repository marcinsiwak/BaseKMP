package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.domain.game.AddPlayerToGameUseCase
import pl.msiwak.domain.game.DisconnectUseCase
import pl.msiwak.domain.game.FindGameIPAddressUseCase
import pl.msiwak.domain.game.ObservePlayersConnectionUseCase
import pl.msiwak.domain.game.RemovePlayerFromGameUseCase
import pl.msiwak.domain.game.CreateGameUseCase
import pl.msiwak.domain.game.FinishGameUseCase
import pl.msiwak.domainimpl.game.AddPlayerToGameUseCaseImpl
import pl.msiwak.domainimpl.game.DisconnectUseCaseImpl
import pl.msiwak.domainimpl.game.FindGameIPAddressUseCaseImpl
import pl.msiwak.domainimpl.game.ObservePlayersConnectionUseCaseImpl
import pl.msiwak.domainimpl.game.RemovePlayerFromGameUseCaseImpl
import pl.msiwak.domainimpl.game.CreateGameUseCaseImpl
import pl.msiwak.domainimpl.game.FinishGameUseCaseImpl

internal val useCaseModule = module {
    factory<CreateGameUseCase> { CreateGameUseCaseImpl(get()) }
    factory<FinishGameUseCase> { FinishGameUseCaseImpl(get()) }
    factory<AddPlayerToGameUseCase> { AddPlayerToGameUseCaseImpl(get()) }
    factory<RemovePlayerFromGameUseCase> { RemovePlayerFromGameUseCaseImpl(get()) }
    factory<ObservePlayersConnectionUseCase> { ObservePlayersConnectionUseCaseImpl(get()) }
    factory<FindGameIPAddressUseCase> { FindGameIPAddressUseCaseImpl(get()) }
    factory<DisconnectUseCase> { DisconnectUseCaseImpl(get()) }
}
