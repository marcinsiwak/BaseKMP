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
import pl.msiwak.domain.game.AddPlayerToGameUseCase
import pl.msiwak.domain.game.DisconnectUseCase
import pl.msiwak.domain.game.FindGameIPAddressUseCase
import pl.msiwak.domain.game.ObservePlayersConnectionUseCase
import pl.msiwak.domain.game.StartGameUseCase
import pl.msiwak.domain.game.StopGameUseCase

class GameViewModel(
    private val startGameUseCase: StartGameUseCase,
    private val stopGameUseCase: StopGameUseCase,
    private val addPlayerToGameUseCase: AddPlayerToGameUseCase,
    private val observePlayersConnectionUseCase: ObservePlayersConnectionUseCase,
    private val findGameIPAddressUseCase: FindGameIPAddressUseCase,
    private val disconnectUseCase: DisconnectUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        print(throwable)
    }

    init {
        viewModelScope.launch(errorHandler) {
            val existingGameIpAddress = findGameIPAddressUseCase()
            _uiState.update { it.copy(gameIpAddress = existingGameIpAddress) }
            observeWebSocketEvents()
        }
    }

    fun onUiAction(action: GameUiAction) {
        when (action) {
            is GameUiAction.OnBackClicked -> {
                // Handle back navigation
            }

            is GameUiAction.StartSession -> onStartSession()
            is GameUiAction.StopSession -> viewModelScope.launch(errorHandler) { stopGameUseCase() }
            is GameUiAction.Connect -> viewModelScope.launch(errorHandler) {
                addPlayerToGameUseCase(
                    host = uiState.value.gameIpAddress ?: throw Exception("Game IP address not found"),
                    playerName = "Marcin"
                )
            }

            is GameUiAction.Disconnect -> viewModelScope.launch(errorHandler) { disconnectUseCase() }
            is GameUiAction.Refresh -> viewModelScope.launch(errorHandler) {
                val existingGameIpAddress = findGameIPAddressUseCase()
                _uiState.update { it.copy(gameIpAddress = existingGameIpAddress) }
            }
        }
    }

    private fun onStartSession() {
        viewModelScope.launch(errorHandler) {
            startGameUseCase()
        }
    }

    private suspend fun observeWebSocketEvents() {
        observePlayersConnectionUseCase().collectLatest { event ->
            when (event) {
                is WebSocketEvent.DisplayCurrentUsers -> {
                    _uiState.update { it.copy(players = event.currentPlayers) }
                }

                else -> Unit
            }
        }
    }
}
