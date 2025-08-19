package pl.msiwak.domainimpl.player

import pl.msiwak.common.model.Player
import pl.msiwak.data.player.PlayerRepository
import pl.msiwak.domain.player.ConnectPlayerUseCase

class ConnectPlayerUseCaseImpl(private val playerRepository: PlayerRepository) : ConnectPlayerUseCase {
    override suspend fun invoke(player: Player): Player {
        return playerRepository.connectPlayer(player)
    }
}
