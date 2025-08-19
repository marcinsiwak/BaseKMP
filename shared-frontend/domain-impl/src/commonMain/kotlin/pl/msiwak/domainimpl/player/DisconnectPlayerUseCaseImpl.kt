package pl.msiwak.domainimpl.player

import pl.msiwak.data.player.PlayerRepository
import pl.msiwak.domain.player.DisconnectPlayerUseCase

class DisconnectPlayerUseCaseImpl(private val playerRepository: PlayerRepository) : DisconnectPlayerUseCase {
    override suspend fun invoke(playerId: String): Boolean {
        return playerRepository.disconnectPlayer(playerId)
    }
}
