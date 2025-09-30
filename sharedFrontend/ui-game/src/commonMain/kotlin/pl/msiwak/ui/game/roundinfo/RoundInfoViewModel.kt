package pl.msiwak.ui.game.roundinfo

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
import pl.msiwak.destination.NavDestination
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.navigator.Navigator

class RoundInfoViewModel(
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoundInfoViewState())
    val uiState: StateFlow<RoundInfoViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeGameSession()
        }
    }

    fun onUiAction(action: RoundInfoUiAction) {
        when (action) {
            is RoundInfoUiAction.OnStartRoundClick -> viewModelScope.launch { navigator.navigate(NavDestination.GameDestination.RoundScreen) }
        }
    }

    private suspend fun observeGameSession() {
        observeGameSessionUseCase().filterNotNull().collectLatest { gameSession ->
            with(gameSession) {
                when (gameState) {
                    GameState.TABOO -> _uiState.update { it.copy(text = "TABOO") }
                    GameState.PUNS -> _uiState.update { it.copy(text = "PUNS") }
                    GameState.TABOO_SHORT -> _uiState.update { it.copy(text = "TABOO_SHORT") }
                    GameState.PUNS_SHORT -> _uiState.update { it.copy(text = "PUNS_SHORT") }
                    else -> Unit
                }
            }
        }
    }

}
