package pl.msiwak.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json
import pl.msiwak.common.model.GameActions
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.GameState
import pl.msiwak.common.model.ServerGameActions
import pl.msiwak.connection.model.ClientActions
import pl.msiwak.connection.model.WebSocketEvent
import pl.msiwak.gamemanager.GameManager

class ServerManager(
    private val ktorServer: KtorServer,
    private val gameManager: GameManager
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

//    suspend fun observeGameSession() {
//        gameManager.currentGameSession.onEach { delay(
//        ktorServer.messages.map {
//            if (it.startsWith("Client disconnected: ")) {
//                ClientActions.UserDisconnected(it.substringAfter("Client disconnected: "))
//            } else {
//                json.decodeFromString<WebSocketEvent>(it)
//            }
//        }
//            .collectLatest { event ->
//                when (event) {
//                    is ClientActions.UserConnected -> gameManager.joinGame(event.player)
//
//                    is GameActions.PlayerGameDisconnected -> {
////                        gameManager.leaveGame(event.id)
//                        gameManager.disablePlayer(event.id)
//                        ktorServer.closeSocker(event.id)
//                    }
//
//                    is WebSocketEvent.ServerActions.UpdateGameSession -> {
//                        gameManager.getGameSession()?.let {
//                            ktorServer.sendMessageToAll(
//                                json.encodeToString<WebSocketEvent>(event)
//                            )
//                        }
//                    }
//
//                    is WebSocketEvent.GameActions.SetPlayerReady -> {
//                        gameManager.setPlayerReady(event.id)
//                    }
//
//                    is WebSocketEvent.GameActions.AddCard -> {
//                        gameManager.addCardToGame(event.id, event.cardText)
//                    }
//
//                    is WebSocketEvent.GameActions.ContinueGame -> {
//                        gameManager.continueGame()
//                    }
//
//                    is WebSocketEvent.GameActions.JoinTeam -> {
//                        gameManager.joinTeam(event.id, event.teamName)
//                    }
//
//                    is WebSocketEvent.GameActions.SetCorrectAnswer -> {
//                        gameManager.setCorrectAnswer(event.cardText)
//                    }
//
//                    is WebSocketEvent.GameActions.AddPlayerName -> {
//                        gameManager.addPlayerName(event.id, event.name)
//                    }
//
//                    else -> Unit
//                }
//            }
//    }
//            100) }.filterNotNull().collectLatest { gameSession ->
//            if (gameSession.gameState == GameState.FINISHED) {
//                ktorServer.closeAllSockets()
//            } else {
//                ktorServer.sendMessageToAll(
//                    json.encodeToString<WebSocketEvent>(
//                        ServerGameActions.UpdateGameSession(gameSession)
//                    )
//                )
//            }
//        }
//    }

//    suspend fun observeMessages() {
    fun startServer(host: String, port: Int) {
        runCatching {
            ktorServer.startServer(host, port)
        }.onFailure {
            println("Server start failed: ${it.message}")
        }
    }

    suspend fun createGame(adminId: String, ipAddress: String?, gameSession: GameSession?) {
        gameManager.createGame(adminId, ipAddress, gameSession)
    }

    suspend fun stopServer() {
        ktorServer.stopServer()
    }

}