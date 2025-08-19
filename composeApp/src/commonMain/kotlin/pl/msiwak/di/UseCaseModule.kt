package pl.msiwak.di

import org.koin.dsl.module
import pl.msiwak.domain.game.AddPlayerToGameUseCase
import pl.msiwak.domain.game.ObservePlayersConnectionUseCase
import pl.msiwak.domain.game.RemovePlayerFromGameUseCase
import pl.msiwak.domain.game.StartGameUseCase
import pl.msiwak.domain.game.StopGameUseCase
import pl.msiwak.domain.player.ConnectPlayerUseCase
import pl.msiwak.domain.player.DisconnectPlayerUseCase
import pl.msiwak.domain.player.GetConnectedPlayersUseCase
import pl.msiwak.domain.player.GetPlayersUseCase
import pl.msiwak.domainimpl.game.AddPlayerToGameUseCaseImpl
import pl.msiwak.domainimpl.game.ObservePlayersConnectionUseCaseImpl
import pl.msiwak.domainimpl.game.RemovePlayerFromGameUseCaseImpl
import pl.msiwak.domainimpl.game.StartGameUseCaseImpl
import pl.msiwak.domainimpl.game.StopGameUseCaseImpl
import pl.msiwak.domainimpl.player.ConnectPlayerUseCaseImpl
import pl.msiwak.domainimpl.player.DisconnectPlayerUseCaseImpl
import pl.msiwak.domainimpl.player.GetConnectedPlayersUseCaseImpl
import pl.msiwak.domainimpl.player.GetPlayersUseCaseImpl

internal val useCaseModule = module {
    single<GetPlayersUseCase> { GetPlayersUseCaseImpl(get()) }
    single<ConnectPlayerUseCase> { ConnectPlayerUseCaseImpl(get()) }
    single<DisconnectPlayerUseCase> { DisconnectPlayerUseCaseImpl(get()) }
    single<GetConnectedPlayersUseCase> { GetConnectedPlayersUseCaseImpl(get()) }
    single<StartGameUseCase> { StartGameUseCaseImpl(get()) }
    single<StopGameUseCase> { StopGameUseCaseImpl(get()) }
    single<AddPlayerToGameUseCase> { AddPlayerToGameUseCaseImpl(get()) }
    single<RemovePlayerFromGameUseCase> { RemovePlayerFromGameUseCaseImpl(get()) }
    single<ObservePlayersConnectionUseCase> { ObservePlayersConnectionUseCaseImpl(get()) }
}
