package pl.msiwak.network

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import pl.msiwak.common.model.GameSession
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
            ktorServer.sendMessageToAll(
                json.encodeToString<WebSocketEvent>(
                    WebSocketEvent.UpdateGameSession(gameSession)
                )
            )
        }
    }

    suspend fun observeMessages() {
        ktorServer.messages.map {
            if (it.startsWith("Client disconnected: ")) {
                WebSocketEvent.PlayerClientDisconnected(it.substringAfter("Client disconnected: "))
            } else {
                json.decodeFromString<WebSocketEvent>(it)
            }
        }
            .collect { event ->
                when (event) {
                    is WebSocketEvent.PlayerConnected -> gameManager.joinGame(event.player)

                    is WebSocketEvent.PlayerClientDisconnected -> {
//                        gameManager.leaveGame(event.id)
                        gameManager.disablePlayer(event.id)
                        ktorServer.closeSocker(event.id)
                    }

                    is WebSocketEvent.GameLobby -> {
                        gameManager.getGameSession()?.let {
                            ktorServer.sendMessage(
                                event.id,
                                json.encodeToString<WebSocketEvent>(
                                    WebSocketEvent.UpdateGameSession(it)
                                )
                            )
                        }
                    }

                    is WebSocketEvent.UpdateGameSession -> {
                        gameManager.getGameSession()?.let {
                            ktorServer.sendMessageToAll(
                                json.encodeToString<WebSocketEvent>(event)
                            )
                        }
                    }
                    is WebSocketEvent.SetPlayerReady -> {
                        gameManager.setPlayerReady(event.id)
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

    fun stopServer() {
        ktorServer.stopServer()
    }

}