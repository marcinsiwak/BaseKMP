package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.domain.game.AddCardUseCase
import pl.msiwak.domain.game.ConnectPlayerToGameUseCase
import pl.msiwak.domain.game.CreateGameUseCase
import pl.msiwak.domain.game.DisconnectUseCase
import pl.msiwak.domain.game.FindGameIPAddressUseCase
import pl.msiwak.domain.game.FinishGameUseCase
import pl.msiwak.domain.game.GetUserIdUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.domain.game.ObserveWebSocketEventsUseCase
import pl.msiwak.domain.game.SendClientEventUseCase
import pl.msiwak.domain.game.SetPlayerReadyUseCase
import pl.msiwak.domainimpl.game.AddCardUseCaseImpl
import pl.msiwak.domainimpl.game.ConnectPlayerToGameUseCaseImpl
import pl.msiwak.domainimpl.game.CreateGameUseCaseImpl
import pl.msiwak.domainimpl.game.DisconnectUseCaseImpl
import pl.msiwak.domainimpl.game.FindGameIPAddressUseCaseImpl
import pl.msiwak.domainimpl.game.FinishGameUseCaseImpl
import pl.msiwak.domainimpl.game.GetUserIdUseCaseImpl
import pl.msiwak.domainimpl.game.ObserveGameSessionUseCaseImpl
import pl.msiwak.domainimpl.game.ObserveWebSocketEventsUseCaseImpl
import pl.msiwak.domainimpl.game.SendClientEventUseCaseImpl
import pl.msiwak.domainimpl.game.SetPlayerReadyUseCaseImpl

internal val useCaseModule = module {
    factory<CreateGameUseCase> { CreateGameUseCaseImpl(get()) }
    factory<FinishGameUseCase> { FinishGameUseCaseImpl(get()) }
    factory<ConnectPlayerToGameUseCase> { ConnectPlayerToGameUseCaseImpl(get()) }
    factory<ObserveWebSocketEventsUseCase> { ObserveWebSocketEventsUseCaseImpl(get()) }
    factory<FindGameIPAddressUseCase> { FindGameIPAddressUseCaseImpl(get()) }
    factory<DisconnectUseCase> { DisconnectUseCaseImpl(get()) }
    factory<SendClientEventUseCase> { SendClientEventUseCaseImpl(get()) }
    factory<GetUserIdUseCase> { GetUserIdUseCaseImpl(get()) }
    factory<ObserveGameSessionUseCase> { ObserveGameSessionUseCaseImpl(get()) }
    factory<SetPlayerReadyUseCase> { SetPlayerReadyUseCaseImpl(get()) }
    factory<AddCardUseCase> { AddCardUseCaseImpl(get()) }
}
