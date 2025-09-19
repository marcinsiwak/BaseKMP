package pl.msiwak.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.domain.game.DisconnectUseCase
import pl.msiwak.domain.game.GetUserIdUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.domain.game.ObserveWebSocketEventsUseCase
import pl.msiwak.domain.game.SendClientEventUseCase
import pl.msiwak.navigator.Navigator

class LobbyViewModel(
    private val observeWebSocketEventsUseCase: ObserveWebSocketEventsUseCase,
    private val disconnectUseCase: DisconnectUseCase,
    private val sendClientEventUseCase: SendClientEventUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyState())
    val uiState: StateFlow<LobbyState> = _uiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        print(throwable)
    }

    init {
        viewModelScope.launch(errorHandler) {
            launch {
                observeWebSocketEventsUseCase()
            }
            launch {
                observeGameSession()
            }
            sendClientEventUseCase(WebSocketEvent.GameLobby(getUserIdUseCase()))
        }
    }

    fun onUiAction(action: LobbyUiAction) {
        when (action) {
            is LobbyUiAction.OnBackClicked -> {
                // Handle back navigation
            }

            is LobbyUiAction.Disconnect -> viewModelScope.launch(errorHandler) {
                disconnectUseCase()
                navigator.navigateUp()
            }
        }
    }

    private suspend fun observeGameSession() {
        observeGameSessionUseCase().collectLatest { gameSession ->
            _uiState.update { it.copy(players = gameSession?.players ?: emptyList(), gameIpAddress = gameSession?.gameServerIpAddress) }
        }
    }
}
