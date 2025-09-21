package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.FinishGameUseCase

class FinishGameUseCaseImpl(private val gameRepository: GameRepository) : FinishGameUseCase {
    override suspend fun invoke() {
        gameRepository.finishGame()
    }
}
