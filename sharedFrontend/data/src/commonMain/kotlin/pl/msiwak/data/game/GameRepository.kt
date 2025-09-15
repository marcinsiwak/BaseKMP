package pl.msiwak.data.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.network.service.GameService

class GameRepository(
    private val gameService: GameService
) {
    private val _currentGameSession = MutableStateFlow<GameSession?>(null)
    val currentGameSession: StateFlow<GameSession?> = _currentGameSession.asStateFlow()

    suspend fun observeWebSocketEvents() {
        gameService.observeWebSocketEvents().collect {
            when (it) {
                is WebSocketEvent.UpdateGameSession -> _currentGameSession.value = it.gameSession
                else -> {}
            }
        }
    }

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

    fun getUserId(): String {
        return gameService.getUserId()
    }

    suspend fun sendClientEvent(webSocketEvent: WebSocketEvent) = gameService.sendClientEvent(webSocketEvent)
}