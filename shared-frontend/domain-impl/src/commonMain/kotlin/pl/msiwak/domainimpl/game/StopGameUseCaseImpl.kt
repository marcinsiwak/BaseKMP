package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.StopGameUseCase

class StopGameUseCaseImpl(private val gameRepository: GameRepository) : StopGameUseCase {
    override suspend fun invoke() {
        gameRepository.stopGame()
    }
}
