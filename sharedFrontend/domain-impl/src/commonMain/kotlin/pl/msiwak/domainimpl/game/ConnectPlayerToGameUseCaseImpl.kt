package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.ConnectPlayerToGameUseCase

class ConnectPlayerToGameUseCaseImpl(private val gameRepository: GameRepository) : ConnectPlayerToGameUseCase {
    override suspend fun invoke(playerName: String) {
        return gameRepository.connectPlayer(playerName)
    }
}
