package pl.msiwak.domain.player

import pl.msiwak.data.player.PlayerRepository

class GetPlayersUseCase(private val repository: PlayerRepository) {
    operator fun invoke() = repository.getPlayers()
}
