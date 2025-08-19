package pl.msiwak.domain.game

import pl.msiwak.common.model.Player

interface AddPlayerToGameUseCase {
    suspend operator fun invoke(player: Player): Player
}
