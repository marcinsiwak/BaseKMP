package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.domain.game.AddCardUseCase
import pl.msiwak.domain.game.CheckWifiIsOnUseCase
import pl.msiwak.domain.game.ConnectPlayerToGameUseCase
import pl.msiwak.domain.game.ContinueGameUseCase
import pl.msiwak.domain.game.DisconnectUseCase
import pl.msiwak.domain.game.ElectServerHostUseCase
import pl.msiwak.domain.game.FindGameIPAddressUseCase
import pl.msiwak.domain.game.FinishGameUseCase
import pl.msiwak.domain.game.GetUserIdUseCase
import pl.msiwak.domain.game.JoinGameUseCase
import pl.msiwak.domain.game.JoinTeamUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.domain.game.ObserveHostIpUseCase
import pl.msiwak.domain.game.ObserveWebSocketEventsUseCase
import pl.msiwak.domain.game.SendClientEventUseCase
import pl.msiwak.domain.game.SetCorrectAnswerUseCase
import pl.msiwak.domain.game.SetPlayerReadyUseCase
import pl.msiwak.domainimpl.game.AddCardUseCaseImpl
import pl.msiwak.domainimpl.game.CheckWifiIsOnUseCaseImpl
import pl.msiwak.domainimpl.game.ConnectPlayerToGameUseCaseImpl
import pl.msiwak.domainimpl.game.ContinueGameUseCaseImpl
import pl.msiwak.domainimpl.game.DisconnectUseCaseImpl
import pl.msiwak.domainimpl.game.ElectServerHostUseCaseImpl
import pl.msiwak.domainimpl.game.FindGameIPAddressUseCaseImpl
import pl.msiwak.domainimpl.game.FinishGameUseCaseImpl
import pl.msiwak.domainimpl.game.GetUserIdUseCaseImpl
import pl.msiwak.domainimpl.game.JoinGameUseCaseImpl
import pl.msiwak.domainimpl.game.JoinTeamUseCaseImpl
import pl.msiwak.domainimpl.game.ObserveGameSessionUseCaseImpl
import pl.msiwak.domainimpl.game.ObserveHostIpUseCaseImpl
import pl.msiwak.domainimpl.game.ObserveWebSocketEventsUseCaseImpl
import pl.msiwak.domainimpl.game.SendClientEventUseCaseImpl
import pl.msiwak.domainimpl.game.SetCorrectAnswerUseCaseImpl
import pl.msiwak.domainimpl.game.SetPlayerReadyUseCaseImpl

internal val useCaseModule = module {
    factory<JoinGameUseCase> { JoinGameUseCaseImpl(get()) }
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
    factory<ContinueGameUseCase> { ContinueGameUseCaseImpl(get()) }
    factory<JoinTeamUseCase> { JoinTeamUseCaseImpl(get(), get()) }
    factory<SetCorrectAnswerUseCase> { SetCorrectAnswerUseCaseImpl(get()) }
    factory<ElectServerHostUseCase> { ElectServerHostUseCaseImpl(get()) }
    factory<ObserveHostIpUseCase> { ObserveHostIpUseCaseImpl(get()) }
    factory<CheckWifiIsOnUseCase> { CheckWifiIsOnUseCaseImpl(get()) }
}
