package pl.msiwak.data.game

import pl.msiwak.network.service.GameService

class GameRepository(
    private val gameService: GameService
) {
    suspend fun observePlayersConnection() = gameService.observePlayersConnection()

    suspend fun findGame(): String? {
        return gameService.findGame()
    }

    suspend fun createGame() {
        gameService.createGame()
    }

    suspend fun finishGame() {
        gameService.finishGame()
    }

    suspend fun addPlayerToGame(host: String, playerName: String) {
        return gameService.connectPlayer(host, playerName)
    }

    suspend fun disconnectPlayer() {
        return gameService.disconnectPlayer()
    }
}