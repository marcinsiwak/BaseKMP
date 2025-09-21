package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.CreateGameUseCase

class CreateGameUseCaseImpl(private val gameRepository: GameRepository) : CreateGameUseCase {
    override suspend fun invoke(adminName: String) {
        gameRepository.createGame(adminName)
    }
}
