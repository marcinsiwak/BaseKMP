package pl.msiwak.network

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.GameState
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.gamemanager.GameManager

class ServerManager(
    private val ktorServer: KtorServer,
    private val gameManager: GameManager
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend fun observeGameSession() {
        gameManager.currentGameSession.filterNotNull().collectLatest { gameSession ->
            if (gameSession.gameState == GameState.FINISHED) {
                ktorServer.closeAllSockets()
            } else {
                ktorServer.sendMessageToAll(
                    json.encodeToString<WebSocketEvent>(
                        WebSocketEvent.ServerActions.UpdateGameSession(gameSession)
                    )
                )
            }
        }
    }

    suspend fun observeMessages() {
        ktorServer.messages.map {
            if (it.startsWith("Client disconnected: ")) {
                WebSocketEvent.ClientActions.PlayerClientDisconnected(it.substringAfter("Client disconnected: "))
            } else {
                json.decodeFromString<WebSocketEvent>(it)
            }
        }
            .collectLatest { event ->
                when (event) {
                    is WebSocketEvent.ClientActions.PlayerConnected -> gameManager.joinGame(event.player)

                    is WebSocketEvent.ClientActions.PlayerClientDisconnected -> {
//                        gameManager.leaveGame(event.id)
                        gameManager.disablePlayer(event.id)
                        ktorServer.closeSocker(event.id)
                    }

                    is WebSocketEvent.ServerActions.UpdateGameSession -> {
                        gameManager.getGameSession()?.let {
                            ktorServer.sendMessageToAll(
                                json.encodeToString<WebSocketEvent>(event)
                            )
                        }
                    }

                    is WebSocketEvent.ClientActions.SetPlayerReady -> {
                        gameManager.setPlayerReady(event.id)
                    }

                    is WebSocketEvent.ClientActions.AddCard -> {
                        gameManager.addCardToGame(event.id, event.cardText)
                    }

                    is WebSocketEvent.ClientActions.ContinueGame -> {
                        gameManager.continueGame()
                    }

                    is WebSocketEvent.ClientActions.JoinTeam -> {
                        gameManager.joinTeam(event.id, event.teamName)
                    }

                    is WebSocketEvent.ClientActions.SetCorrectAnswer -> {
                        gameManager.setCorrectAnswer(event.cardText)
                    }

                    else -> Unit
                }
            }
    }

    suspend fun startServer(host: String, port: Int) {
        ktorServer.startServer(host, port)
    }

    suspend fun createGame(adminId: String, ipAddress: String?, gameSession: GameSession?) {
        gameManager.createGame(adminId, ipAddress, gameSession)
    }

    suspend fun stopServer() {
        ktorServer.stopServer()
    }

}