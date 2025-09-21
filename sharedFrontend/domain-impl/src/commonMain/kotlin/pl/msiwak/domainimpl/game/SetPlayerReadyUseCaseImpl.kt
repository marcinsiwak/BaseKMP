package pl.msiwak.domainimpl.game

import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.DisconnectUseCase
import pl.msiwak.domain.game.SetPlayerReadyUseCase

class SetPlayerReadyUseCaseImpl(private val gameRepository: GameRepository) : SetPlayerReadyUseCase {
    override suspend fun invoke() {
        gameRepository.sendClientEvent(WebSocketEvent.SetPlayerReady(gameRepository.getUserId()))
    }
}
