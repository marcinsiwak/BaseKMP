package pl.msiwak.domainimpl.game

import pl.msiwak.common.model.GameActions
import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.SetCorrectAnswerUseCase

class SetCorrectAnswerUseCaseImpl(private val gameRepository: GameRepository) : SetCorrectAnswerUseCase {
    override suspend fun invoke(cardText: String) {
        gameRepository.sendClientEvent(GameActions.SetCorrectAnswer(cardText))
    }
}
