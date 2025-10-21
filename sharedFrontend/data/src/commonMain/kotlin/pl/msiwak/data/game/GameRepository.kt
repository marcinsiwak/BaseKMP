package pl.msiwak.data.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.GameState
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.network.service.GameService

class GameRepository(
    private val gameService: GameService
) {
    private val _currentGameSession = MutableStateFlow<GameSession?>(null)
    val currentGameSession: StateFlow<GameSession?> = _currentGameSession.asStateFlow()

    suspend fun observeWebSocketEvents() {
        gameService.observeWebSocketEvents().collectLatest {
            when (it) {
                is WebSocketEvent.ServerActions.UpdateGameSession -> {
                    _currentGameSession.value = it.gameSession
                }
                WebSocketEvent.ClientActions.ServerDownDetected -> managePlayerConnection()

                else -> Unit
            }
        }
    }

    suspend fun findGame(): String? {
        return gameService.findGame().first()
    }

    suspend fun createGame(adminName: String) {
        gameService.createGame(adminName)
    }

    suspend fun finishGame() {
        gameService.disconnectPlayer()
    }

    suspend fun connectPlayer(playerName: String) {
        gameService.connectPlayer(playerName)
    }

    suspend fun disconnectPlayer() {
        return gameService.disconnectPlayer()
    }

    fun getUserId(): String {
        return gameService.getUserId()
    }

    suspend fun sendClientEvent(webSocketEvent: WebSocketEvent) = gameService.sendClientEvent(webSocketEvent)

    private suspend fun managePlayerConnection() {
        with(currentGameSession.value ?: return) {
            if (gameState == GameState.SUMMARY)  return
            val currentPlayer = players.first { player -> player.id == gameService.getUserId() }
            findGame()?.let {
                gameService.connectPlayer(currentPlayer.name)
                return
            }
            val playerToBeAdmin = players.first { player -> player.id != adminId }
            if (playerToBeAdmin.id == currentPlayer.id) {
                gameService.createGame(playerToBeAdmin.name, this)
            } else {
                findGame()
                gameService.connectPlayer(currentPlayer.name)
            }
        }
    }
}