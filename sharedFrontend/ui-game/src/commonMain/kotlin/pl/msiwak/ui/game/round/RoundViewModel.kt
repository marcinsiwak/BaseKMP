package pl.msiwak.ui.game.round

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.common.model.GameState
import pl.msiwak.domain.game.GetUserIdUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.navigator.Navigator

class RoundViewModel(
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoundViewState())
    val uiState: StateFlow<RoundViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeGameSession()
        }
    }

    fun onUiAction(action: RoundUiAction) {
        when (action) {
            is RoundUiAction.OnCorrectClick -> {}
            is RoundUiAction.OnSkipClick -> {}
        }
    }

    private suspend fun observeGameSession() {
        observeGameSessionUseCase().filterNotNull().collectLatest { gameSession ->
            with(gameSession) {
                _uiState.update { it.copy(isCurrentPlayerRound = gameSession.currentPlayerId == getUserIdUseCase()) }
                when (gameState) {
                    GameState.TABOO -> _uiState.update { it.copy(text = "TABOO game") }
                    GameState.PUNS -> _uiState.update { it.copy(text = "PUNS game") }
                    GameState.TABOO_SHORT -> _uiState.update { it.copy(text = "TABOO_SHORT game") }
                    GameState.PUNS_SHORT -> _uiState.update { it.copy(text = "PUNS_SHORT game") }
                    else -> Unit
                }
            }
        }
    }
}
