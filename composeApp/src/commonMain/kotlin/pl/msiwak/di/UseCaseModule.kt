package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.domain.game.AddPlayerToGameUseCase
import pl.msiwak.domain.game.CreateGameUseCase
import pl.msiwak.domain.game.DisconnectUseCase
import pl.msiwak.domain.game.FindGameIPAddressUseCase
import pl.msiwak.domain.game.FinishGameUseCase
import pl.msiwak.domain.game.GetUserIdUseCase
import pl.msiwak.domain.game.ObserveWebSocketEventsUseCase
import pl.msiwak.domain.game.SendClientEventUseCase
import pl.msiwak.domainimpl.game.AddPlayerToGameUseCaseImpl
import pl.msiwak.domainimpl.game.CreateGameUseCaseImpl
import pl.msiwak.domainimpl.game.DisconnectUseCaseImpl
import pl.msiwak.domainimpl.game.FindGameIPAddressUseCaseImpl
import pl.msiwak.domainimpl.game.FinishGameUseCaseImpl
import pl.msiwak.domainimpl.game.GetUserIdUseCaseImpl
import pl.msiwak.domainimpl.game.ObserveWebSocketEventsUseCaseImpl
import pl.msiwak.domainimpl.game.SendClientEventUseCaseImpl

internal val useCaseModule = module {
    factory<CreateGameUseCase> { CreateGameUseCaseImpl(get()) }
    factory<FinishGameUseCase> { FinishGameUseCaseImpl(get()) }
    factory<AddPlayerToGameUseCase> { AddPlayerToGameUseCaseImpl(get()) }
    factory<ObserveWebSocketEventsUseCase> { ObserveWebSocketEventsUseCaseImpl(get()) }
    factory<FindGameIPAddressUseCase> { FindGameIPAddressUseCaseImpl(get()) }
    factory<DisconnectUseCase> { DisconnectUseCaseImpl(get()) }
    factory<SendClientEventUseCase> { SendClientEventUseCaseImpl(get()) }
    factory<GetUserIdUseCase> { GetUserIdUseCaseImpl(get()) }
}
