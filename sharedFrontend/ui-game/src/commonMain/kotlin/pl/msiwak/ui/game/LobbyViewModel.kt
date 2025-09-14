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
import pl.msiwak.domain.game.ObserveWebSocketEventsUseCase
import pl.msiwak.domain.game.SendClientEventUseCase

class LobbyViewModel(
    private val observeWebSocketEventsUseCase: ObserveWebSocketEventsUseCase,
    private val disconnectUseCase: DisconnectUseCase,
    private val sendClientEventUseCase: SendClientEventUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyState())
    val uiState: StateFlow<LobbyState> = _uiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        print(throwable)
    }

    init {
        viewModelScope.launch(errorHandler) {
            observeWebSocketEvents()
            sendClientEventUseCase(WebSocketEvent.GameLobby(getUserIdUseCase()))
        }
    }

    fun onUiAction(action: LobbyUiAction) {
        when (action) {
            is LobbyUiAction.OnBackClicked -> {
                // Handle back navigation
            }
            is LobbyUiAction.Disconnect -> viewModelScope.launch(errorHandler) { disconnectUseCase() }
        }
    }

    private suspend fun observeWebSocketEvents() {
        observeWebSocketEventsUseCase().collectLatest { event ->
            when (event) {
                is WebSocketEvent.DisplayCurrentUsers -> {
                    _uiState.update { it.copy(players = event.currentPlayers) }
                }

                else -> Unit
            }
        }
    }
}
