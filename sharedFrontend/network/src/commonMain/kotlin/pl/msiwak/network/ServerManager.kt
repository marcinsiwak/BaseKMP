package pl.msiwak.network

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import pl.msiwak.common.model.Player
import pl.msiwak.common.model.WebSocketEvent

class ServerManager(
    private val ktorServer: KtorServer
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val players = mutableListOf<Player>()

    suspend fun observeMessages() {
        ktorServer.messages.map {
            json.decodeFromString<WebSocketEvent>(it)
        }
            .filter { !(it is WebSocketEvent.PlayerConnected && players.contains(it.player)) }
            .collect { event ->
                when (event) {
                    is WebSocketEvent.PlayerConnected -> {
                        players.add(event.player)
                        ktorServer.sendMessageToAll(
                            json.encodeToString<WebSocketEvent>(
                                WebSocketEvent.DisplayCurrentUsers(
                                    players
                                )
                            )
                        )
                    }

                    is WebSocketEvent.PlayerClientDisconnected -> {
                        players.removeAll { player -> player.id == event.id }
                        ktorServer.sendMessageToAll(
                            json.encodeToString<WebSocketEvent>(
                                WebSocketEvent.DisplayCurrentUsers(
                                    players
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