package pl.msiwak.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.common.model.Player
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.domain.game.AddPlayerToGameUseCase
import pl.msiwak.domain.game.ObservePlayersConnectionUseCase
import pl.msiwak.domain.game.StartGameUseCase
import pl.msiwak.domain.game.StopGameUseCase

class GameViewModel(
    private val startGameUseCase: StartGameUseCase,
    private val stopGameUseCase: StopGameUseCase,
    private val addPlayerToGameUseCase: AddPlayerToGameUseCase,
    private val observePlayersConnectionUseCase: ObservePlayersConnectionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observePlayersConnectionUseCase().collectLatest { event ->
                when (event) {
                    is WebSocketEvent.PlayerConnection.PlayerConnected -> {
                        _uiState.update { it.copy(players = event.currentPlayers) }
                    }

                    is WebSocketEvent.PlayerConnection.PlayerDisconnected -> {
                        _uiState.update { it.copy(players = event.currentPlayers) }

                    }
                }
            }
        }
    }

    fun onUiAction(action: GameUiAction) {
        when (action) {
            is GameUiAction.OnBackClicked -> {
                // Handle back navigation
            }

            is GameUiAction.StartSession -> viewModelScope.launch { startGameUseCase() }
            is GameUiAction.StopSession -> viewModelScope.launch { stopGameUseCase() }
            is GameUiAction.Connect -> viewModelScope.launch { addPlayerToGameUseCase(Player(name = "Marcin")) }
        }
    }
}
