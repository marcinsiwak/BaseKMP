package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.StartGameUseCase

class StartGameUseCaseImpl(private val gameRepository: GameRepository) : StartGameUseCase {
    override suspend fun invoke() {
        gameRepository.startGame()
    }
}
