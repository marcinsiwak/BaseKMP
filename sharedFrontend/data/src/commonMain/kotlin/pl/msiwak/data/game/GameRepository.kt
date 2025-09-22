package pl.msiwak.data.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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
                WebSocketEvent.ServerDown -> {
                    with(currentGameSession.value ?: return@collect) {
                        val currentPlayer = players.first { player -> player.id == gameService.getUserId() }
                        findGame()?.let {
                            gameService.connectPlayer(currentPlayer.name)
                            return@collect
                        }
                        val playerToBeAdmin = players.first { player -> player.id != adminId }
                        if (playerToBeAdmin.id == currentPlayer.id) {
                            gameService.createGame(playerToBeAdmin.name)
                        } else {
                            findGame()
                            gameService.connectPlayer(currentPlayer.name)
                        }
                    }
                }

                else -> {}
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
        gameService.finishGame()
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
}