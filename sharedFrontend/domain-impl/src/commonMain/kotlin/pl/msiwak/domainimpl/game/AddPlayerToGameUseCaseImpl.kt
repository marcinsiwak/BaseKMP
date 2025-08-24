package pl.msiwak.domainimpl.game

import pl.msiwak.common.model.Player
import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.AddPlayerToGameUseCase

class AddPlayerToGameUseCaseImpl(private val gameRepository: GameRepository) : AddPlayerToGameUseCase {
    override suspend fun invoke(host: String, player: Player) {
        return gameRepository.addPlayerToGame(host, player.name)
    }
}
