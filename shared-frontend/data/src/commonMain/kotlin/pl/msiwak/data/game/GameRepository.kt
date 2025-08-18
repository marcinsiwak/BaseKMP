package pl.msiwak.data.game

import pl.msiwak.network.service.GameService

class GameRepository(private val gameService: GameService) {

    suspend fun startGame() {
        gameService.startGame()
    }

    suspend fun stopGame() {
        gameService.stopGame()
    }
}