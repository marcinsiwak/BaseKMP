package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.JoinGameUseCase

class JoinGameUseCaseImpl(private val gameRepository: GameRepository) : JoinGameUseCase {
    override suspend fun invoke(adminName: String) {
        gameRepository.joinGame(adminName)
    }
}
