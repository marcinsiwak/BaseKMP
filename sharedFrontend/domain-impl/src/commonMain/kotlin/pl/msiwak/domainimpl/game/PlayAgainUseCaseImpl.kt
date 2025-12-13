package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.PlayAgainUseCase

class PlayAgainUseCaseImpl(private val gameRepository: GameRepository) : PlayAgainUseCase {
    override suspend fun invoke() {
        gameRepository.clearGame()
        gameRepository.connectPlayer()
    }
}
