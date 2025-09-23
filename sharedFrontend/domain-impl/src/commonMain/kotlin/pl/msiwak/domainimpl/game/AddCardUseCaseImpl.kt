package pl.msiwak.domainimpl.game

import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.AddCardUseCase

class AddCardUseCaseImpl(private val gameRepository: GameRepository) : AddCardUseCase {
    override suspend fun invoke(cardText: String) {
        gameRepository.sendClientEvent(WebSocketEvent.ClientActions.AddCard(cardText))
    }
}
