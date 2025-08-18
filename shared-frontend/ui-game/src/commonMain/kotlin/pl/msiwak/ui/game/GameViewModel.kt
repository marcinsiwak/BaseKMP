package pl.msiwak.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.msiwak.domain.session.StartSessionUseCase
import pl.msiwak.domain.session.StopSessionUseCase

class GameViewModel(
    private val startSessionUseCase: StartSessionUseCase,
    private val stopSessionUseCase: StopSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    fun onUiAction(action: GameUiAction) {
        when (action) {
            is GameUiAction.OnBackClicked -> {
                // Handle back navigation
            }

            is GameUiAction.StartSession -> viewModelScope.launch { startSessionUseCase() }
            is GameUiAction.StopSession -> viewModelScope.launch { stopSessionUseCase() }
        }
    }
}
