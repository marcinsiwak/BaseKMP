package pl.msiwak.data.player

import kotlinx.coroutines.withContext
import pl.msiwak.common.model.Player
import pl.msiwak.common.model.dispatcher.Dispatchers
import pl.msiwak.network.service.PlayerService
import pl.msiwak.shared.model.player.ApiPlayer

class PlayerRepository(private val playerService: PlayerService) {

    suspend fun getPlayers(): List<Player> = withContext(Dispatchers.IO) {
        return@withContext playerService.getPlayers().map {
            Player(it.id, it.name)
        }
    }

    suspend fun connectPlayer(player: Player): Player = withContext(Dispatchers.IO) {
        val connectedPlayer = player
        val player = ApiPlayer(id = connectedPlayer.id, name = connectedPlayer.name)
        return@withContext with(playerService.connectPlayer(player)) {
            Player(name = "test")
        }
    }

    suspend fun disconnectPlayer(playerId: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext playerService.disconnectPlayer(playerId)
    }

    suspend fun getConnectedPlayers(): List<Player> = withContext(Dispatchers.IO) {
        return@withContext playerService.getConnectedPlayers().map {
            Player(it.id, it.name)
        }
    }
}
