package pl.msiwak.network

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import pl.msiwak.common.model.Player
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

    suspend fun observeMessages() {
        ktorServer.messages.map {
            json.decodeFromString<WebSocketEvent>(it)
        }
            .filter { !(it is WebSocketEvent.PlayerConnected && gameManager.getPlayers().contains(it.player)) }
            .collect { event ->
                when (event) {
                    is WebSocketEvent.PlayerConnected -> {
                        gameManager.joinGame(event.player)
                        ktorServer.sendMessageToAll(
                            json.encodeToString<WebSocketEvent>(
                                WebSocketEvent.DisplayCurrentUsers(
                                    gameManager.getPlayers()
                                )
                            )
                        )
                    }

                    is WebSocketEvent.PlayerClientDisconnected -> {
                        gameManager.leaveGame(event.id)
                        ktorServer.sendMessageToAll(
                            json.encodeToString<WebSocketEvent>(
                                WebSocketEvent.DisplayCurrentUsers(
                                    gameManager.getPlayers()
                                )
                            )
                        )
                        ktorServer.closeSocker(event.id)
                    }

                    else -> Unit
                }
            }
    }

    fun startServer(host: String, port: Int) {
        ktorServer.startServer(host, port)
    }

    fun stopServer() {
        ktorServer.stopServer()
    }

}