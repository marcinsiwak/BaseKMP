package pl.msiwak.data.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.GameState
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.globalloadermanager.GlobalLoaderManager
import pl.msiwak.network.service.ElectionService
import pl.msiwak.network.service.GameService

class GameRepository(
    private val gameService: GameService,
    private val electionService: ElectionService,
    private val globalLoaderManager: GlobalLoaderManager
) {
    private val _currentGameSession = MutableStateFlow<GameSession?>(null)
    val currentGameSession: StateFlow<GameSession?> = _currentGameSession.asStateFlow()

    private var hostIp: String? = null

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

    suspend fun startElection() {
        electionService.startElection()
    }

    suspend fun observeElectionHostIp() {
        val localIP = gameService.getDeviceIpAddress() ?: throw Exception("Cannot get local IP address")
        electionService.hostIp.collectLatest {
            hostIp = it
            println("Observed new host IP: $it")
            if (it == localIP) gameService.startServer(currentGameSession.value)
        }
    }

    suspend fun findGame(): String? {
        return gameService.findGame().first()
    }

    suspend fun joinGame(playerName: String) {
        hostIp ?: throw Exception("Host IP is not known")
        gameService.connectPlayer(playerName, hostIp)
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
            if (gameState == GameState.SUMMARY) return
            val currentPlayer = players.first { player -> player.id == gameService.getUserId() }
            globalLoaderManager.showLoading()
            findGame()?.let {
                gameService.connectPlayer(currentPlayer.name)
                globalLoaderManager.hideLoading()
                return
            }
        }
    }
}