package pl.msiwak.domain.player

import pl.msiwak.common.model.Player

interface GetConnectedPlayersUseCase {
    suspend operator fun invoke(): List<Player>
}
