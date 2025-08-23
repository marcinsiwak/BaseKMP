package pl.msiwak.domain.player

import pl.msiwak.common.model.Player

interface GetPlayersUseCase {
    suspend fun invoke(): List<Player>
}
