package pl.msiwak.data.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pl.msiwak.common.model.GameActions
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.GameState
import pl.msiwak.common.model.ServerGameActions
import pl.msiwak.common.model.dispatcher.Dispatchers
import pl.msiwak.connection.di.MyConnectionDI
import pl.msiwak.connection.model.ClientActions
import pl.msiwak.connection.model.ServerActions
import pl.msiwak.connection.model.WebSocketEvent
import pl.msiwak.globalloadermanager.GlobalLoaderManager
import pl.msiwak.globalloadermanager.GlobalLoaderMessageType

class GameRepository(
    private val globalLoaderManager: GlobalLoaderManager,
    private val serverManager: ServerManager
) {

    private val myConnection = MyConnectionDI.getMyConnection()
    private val _currentGameSession = MutableStateFlow<GameSession?>(null)
    val currentGameSession: StateFlow<GameSession?> = _currentGameSession.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            launch { observeClientMessages() }
            launch {
                myConnection.serverMessages.collectLatest { event ->
                    when (event) {
                        ServerActions.ServerStarted -> serverManager.start(
                            this,
                            currentGameSession.value
                        )
                    }
                }
            }
            launch {
                myConnection.isLoading.collectLatest { isLoading ->
                    if (isLoading && (currentGameSession.value?.gameState != GameState.FINISHED && currentGameSession.value?.gameState != GameState.SUMMARY)) {
                        globalLoaderManager.showLoading(GlobalLoaderMessageType.DEFAULT)
                    } else {
                        globalLoaderManager.hideLoading()
                    }
                }
            }
        }
    }

    private suspend fun observeClientMessages() {
        myConnection.clientMessages.collect {
            when (it) {
                is ServerGameActions.UpdateGameSession -> {
                    _currentGameSession.value = it.gameSession
                    globalLoaderManager.hideLoading()
                }

                is ClientActions.ServerDownDetected -> {
                    globalLoaderManager.showLoading(
                        GlobalLoaderMessageType.MISSING_HOST
                    )
                }
            }
        }
    }

    suspend fun joinGame(playerName: String) {
        sendClientEvent(GameActions.AddPlayerName(getUserId(), playerName))
    }

    suspend fun finishGame() {
        myConnection.disconnectUsers()
    }


    suspend fun connectPlayer() = myConnection.connect()

    fun getUserId(): String = myConnection.getDeviceId()


    suspend fun sendClientEvent(webSocketEvent: WebSocketEvent) = myConnection.sendFromClient(webSocketEvent)
}