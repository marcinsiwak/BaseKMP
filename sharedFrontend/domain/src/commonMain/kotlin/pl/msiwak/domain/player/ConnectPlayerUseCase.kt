package pl.msiwak.domain.player

import pl.msiwak.common.model.Player

interface ConnectPlayerUseCase {
    suspend operator fun invoke(player: Player): Player
}
