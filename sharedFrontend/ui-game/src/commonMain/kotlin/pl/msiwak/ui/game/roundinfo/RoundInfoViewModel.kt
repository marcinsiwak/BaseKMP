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
import pl.msiwak.domain.game.ContinueGameUseCase
import pl.msiwak.domain.game.GetUserIdUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase

class RoundInfoViewModel(
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val continueGameUseCase: ContinueGameUseCase
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
            is RoundInfoUiAction.OnStartRoundClick -> viewModelScope.launch { continueGameUseCase() }
        }
    }

    private suspend fun observeGameSession() {
        observeGameSessionUseCase().filterNotNull().collectLatest { gameSession ->
            with(gameSession) {
                _uiState.update {
                    it.copy(
                        isCurrentPlayerRound = currentPlayerId == getUserIdUseCase(),
                        currentPlayerName = players.find { player -> player.id == currentPlayerId }?.name ?: "",
                    )
                }
                when (gameState) {
                    GameState.TABOO_INFO -> _uiState.update { it.copy(round = 1, text = "TABOO: Players must describe the card without saying the card’s words or obvious related terms.") }
                    GameState.PUNS_INFO -> _uiState.update { it.copy(round = 2, text = "PUNS: Players can say anything to describe the card (no direct use of the card’s words).") }
                    GameState.TABOO_SHORT_INFO -> _uiState.update { it.copy(round = 3, text = "TABOO_SHORT: Players can say just one single word to describe the card.") }
                    GameState.PUNS_SHORT_INFO -> _uiState.update { it.copy(round = 4, text = "PUNS_SHORT: Players act out the card with only one gesture, no words or sounds.") }
                    else -> Unit
                }
            }
        }
    }

}
