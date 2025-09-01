package pl.msiwak.network

import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import pl.msiwak.common.model.WebSocketEvent

class ServerManager(
    private val ktorServer: KtorServer
) {
    private val json = Json { ignoreUnknownKeys = true }

    private val players = mutableListOf<String>()

    suspend fun observeMessages() {
        ktorServer.messages.map {
            if (it.contains("Player connected: ")) {
                val id = it.substringAfter("Player connected: ")
                WebSocketEvent.PlayerConnected(id)
            }
            else {
                json.decodeFromString<WebSocketEvent>(it)
            }
        }.collect { event ->
            when (event) {
                is WebSocketEvent.PlayerConnected -> {
                    players.add(event.id)
                    ktorServer.sendMessageToAll(json.encodeToString<WebSocketEvent>(WebSocketEvent.DisplayCurrentUsers(players)))
                }

                is WebSocketEvent.PlayerClientDisconnected -> {
                    players.remove(event.id)
                    ktorServer.sendMessageToAll(json.encodeToString<WebSocketEvent>(WebSocketEvent.DisplayCurrentUsers(players)))
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