package pl.msiwak.data.game

import pl.msiwak.common.model.Player
import pl.msiwak.data.player.PlayerRepository
import pl.msiwak.network.service.GameService

class GameRepository(
    private val gameService: GameService,
    private val playerRepository: PlayerRepository
) {
    suspend fun observePlayersConnection() = gameService.observePlayersConnection()

    suspend fun startGame() {
        gameService.startGame()
    }

    suspend fun stopGame() {
        gameService.stopGame()
        // Disconnect all players when game stops
        val connectedPlayers = playerRepository.getConnectedPlayers()
        connectedPlayers.forEach { player ->
            playerRepository.disconnectPlayer(player.id)
        }
    }

    suspend fun addPlayerToGame(player: Player): Player {
        return playerRepository.connectPlayer(player)
    }

    suspend fun removePlayerFromGame(playerId: String): Boolean {
        return playerRepository.disconnectPlayer(playerId)
    }

    suspend fun getGamePlayers(): List<Player> {
        return playerRepository.getConnectedPlayers()
    }
}