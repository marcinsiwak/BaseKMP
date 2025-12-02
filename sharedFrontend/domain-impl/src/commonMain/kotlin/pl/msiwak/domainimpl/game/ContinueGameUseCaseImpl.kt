package pl.msiwak.domainimpl.game

import pl.msiwak.common.model.GameActions
import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.ContinueGameUseCase

class ContinueGameUseCaseImpl(private val gameRepository: GameRepository) : ContinueGameUseCase {
    override suspend fun invoke() {
        gameRepository.sendClientEvent(GameActions.ContinueGame)
    }
}
