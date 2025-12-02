package pl.msiwak.connection

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json
import pl.msiwak.connection.model.WebSocketEvent
//
//class ServerManager(
//    private val myConnection: MyConnection,
//    private val gameManager: GameManager
//) {
//    private val json = Json {
//        ignoreUnknownKeys = true
//        isLenient = true
//    }
//
//    suspend fun observeGameSession() {
//        gameManager.currentGameSession.onEach { delay(100) }.filterNotNull().collectLatest { gameSession ->
//            if (gameSession.gameState == GameState.FINISHED) {
//                ktorServer.closeAllSockets()
//            } else {
//                ktorServer.sendMessageToAll(
//                    json.encodeToString<WebSocketEvent>(
//                        WebSocketEvent.ServerActions.UpdateGameSession(gameSession)
//                    )
//                )
//            }
//        }
//    }
//
//    suspend fun observeMessages() {
//        myConnection.serverMessages
//            .collectLatest { event ->
//                when (event) {
//                    is WebSocketEvent.ClientActions.UserConnected -> gameManager.joinGame(event.player)
//
//                    is WebSocketEvent.ClientActions.UserDisconnected -> {
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
//                    is WebSocketEvent.ClientActions.SetPlayerReady -> {
//                        gameManager.setPlayerReady(event.id)
//                    }
//
//                    is WebSocketEvent.ClientActions.AddCard -> {
//                        gameManager.addCardToGame(event.id, event.cardText)
//                    }
//
//                    is WebSocketEvent.ClientActions.ContinueGame -> {
//                        gameManager.continueGame()
//                    }
//
//                    is WebSocketEvent.ClientActions.JoinTeam -> {
//                        gameManager.joinTeam(event.id, event.teamName)
//                    }
//
//                    is WebSocketEvent.ClientActions.SetCorrectAnswer -> {
//                        gameManager.setCorrectAnswer(event.cardText)
//                    }
//
//                    is WebSocketEvent.ClientActions.AddPlayerName -> {
//                        gameManager.addPlayerName(event.id, event.name)
//                    }
//
//                    else -> Unit
//                }
//            }
//    }
//
//    fun startServer(host: String, port: Int) {
//        runCatching {
//            ktorServer.startServer(host, port)
//        }.onFailure {
//            println("Server start failed: ${it.message}")
//        }
//    }
//
//    suspend fun createGame(adminId: String, ipAddress: String?, gameSession: GameSession?) {
//        gameManager.createGame(adminId, ipAddress, gameSession)
//    }
//
//    suspend fun stopServer() {
//        ktorServer.stopServer()
//    }
//
//}