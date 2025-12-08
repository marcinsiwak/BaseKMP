package pl.msiwak.data.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pl.msiwak.common.model.GameActions
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.GameState
import pl.msiwak.common.model.ServerGameActions
import pl.msiwak.connection.di.MyConnectionDI
import pl.msiwak.connection.model.ClientActions
import pl.msiwak.gamemanager.GameManager

class ServerManager(
    private val gameManager: GameManager
) {
    private val myConnection = MyConnectionDI.getMyConnection()

    private var localGameSession: GameSession? = null

    private var startJob: Job? = null

    suspend fun start(scope: CoroutineScope, gameSession: GameSession?) = with(scope) {
        localGameSession = gameSession
        if (startJob?.isActive == true) return@with
        startJob = launch {
            launch { observeGameSession() }
            launch { observeMessages() }
        }
    }

    suspend fun observeGameSession() {
        gameManager.currentGameSession.onEach { delay(100) }.filterNotNull().collectLatest { gameSession ->
            if (gameSession.gameState == GameState.FINISHED) {
                myConnection.disconnectUsers()
            } else {
                myConnection.sendToAll(ServerGameActions.UpdateGameSession(gameSession))
            }
        }
    }

    suspend fun observeMessages() {
        myConnection.serverMessages
            .collectLatest { event ->
                when (event) {
                    is ClientActions.UserConnected -> {
                        if (event.isHost) {
                            gameManager.createGame(event.id, null, localGameSession)
                        }
                        gameManager.joinGame(event.id)
                    }

                    is ClientActions.UserDisconnected -> gameManager.disablePlayer(event.id)


                    is ServerGameActions.UpdateGameSession -> {
                        gameManager.getGameSession()?.let {
                            myConnection.sendToAll(event)
                        }
                    }

                    is GameActions.SetPlayerReady -> {
                        gameManager.setPlayerReady(event.id)
                    }

                    is GameActions.AddCard -> {
                        gameManager.addCardToGame(event.id, event.cardText)
                    }

                    is GameActions.ContinueGame -> {
                        gameManager.continueGame()
                    }

                    is GameActions.JoinTeam -> {
                        gameManager.joinTeam(event.id, event.teamName)
                    }

                    is GameActions.SetCorrectAnswer -> {
                        gameManager.setCorrectAnswer(event.cardText)
                    }

                    is GameActions.AddPlayerName -> gameManager.addPlayerName(event.id, event.name)

                    else -> Unit
                }
            }
    }

//    suspend fun stopServer() {
//        ktorServer.stopServer()
//    }

}