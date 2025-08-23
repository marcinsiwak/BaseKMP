package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.RemovePlayerFromGameUseCase

class RemovePlayerFromGameUseCaseImpl(private val gameRepository: GameRepository) : RemovePlayerFromGameUseCase {
    override suspend fun invoke(playerId: String): Boolean {
        return gameRepository.removePlayerFromGame(playerId)
    }
}
