package pl.msiwak.domainimpl.player

import pl.msiwak.common.model.Player
import pl.msiwak.data.player.PlayerRepository
import pl.msiwak.domain.player.GetPlayersUseCase

class GetPlayersUseCaseImpl(private val playerRepository: PlayerRepository) : GetPlayersUseCase {
    override suspend fun invoke(): List<Player> {
        return playerRepository.getPlayers()
    }
}
