package pl.msiwak.domainimpl.player

import pl.msiwak.common.model.Player
import pl.msiwak.data.player.PlayerRepository
import pl.msiwak.domain.player.GetConnectedPlayersUseCase

class GetConnectedPlayersUseCaseImpl(private val playerRepository: PlayerRepository) : GetConnectedPlayersUseCase {
    override suspend fun invoke(): List<Player> {
        return playerRepository.getConnectedPlayers()
    }
}
