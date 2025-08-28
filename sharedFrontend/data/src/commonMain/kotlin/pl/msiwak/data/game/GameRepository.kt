package pl.msiwak.data.game

import pl.msiwak.common.model.Player
import pl.msiwak.network.service.GameService

class GameRepository(
    private val gameService: GameService
) {
    suspend fun observePlayersConnection() = gameService.observePlayersConnection()

    suspend fun findGame(): String? {
        return gameService.findGame()
    }

    suspend fun startGame() {
        gameService.startGame()
    }

    suspend fun stopGame() {
        gameService.stopGame()
    }

    suspend fun addPlayerToGame(host: String, playerName: String) {
        return gameService.connectPlayer(host, playerName)
    }

    suspend fun disconnectPlayer() {
        return gameService.disconnectPlayer()
    }

    suspend fun removePlayerFromGame(playerId: String): Boolean {
//        return playerRepository.disconnectPlayer(playerId)
        return true
    }
}