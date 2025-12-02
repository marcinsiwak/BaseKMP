package pl.msiwak.domainimpl.game

import pl.msiwak.common.model.GameActions
import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.AddCardUseCase

class AddCardUseCaseImpl(private val gameRepository: GameRepository) : AddCardUseCase {
    override suspend fun invoke(cardText: String) {
        gameRepository.sendClientEvent(GameActions.AddCard(gameRepository.getUserId(), cardText))
    }
}
