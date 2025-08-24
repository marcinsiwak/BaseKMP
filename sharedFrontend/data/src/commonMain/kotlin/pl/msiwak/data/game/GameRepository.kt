package pl.msiwak.data.game

import pl.msiwak.common.model.Player
import pl.msiwak.network.service.GameService

class GameRepository(
    private val gameService: GameService
) {
    suspend fun observePlayersConnection() = gameService.observePlayersConnection()

    suspend fun findGame(): String {
        return gameService.findGame()
    }

    suspend fun startGame() {
        gameService.startGame()
    }

    suspend fun stopGame() {
        gameService.stopGame()
        // Disconnect all players when game stops
//        val connectedPlayers = playerRepository.getConnectedPlayers()
//        connectedPlayers.forEach { player ->
//            playerRepository.disconnectPlayer(player.id)
//        }
    }

    suspend fun addPlayerToGame(host: String, name: String) {
        return gameService.connectPlayer(host, name)
    }

    suspend fun removePlayerFromGame(playerId: String): Boolean {
//        return playerRepository.disconnectPlayer(playerId)
        return true
    }

    suspend fun getGamePlayers(): List<Player> {
//        return playerRepository.getConnectedPlayers()
        return emptyList()
    }
}