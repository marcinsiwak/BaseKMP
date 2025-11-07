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
                        isPlayerRound = gameSession.currentPlayerId == getUserIdUseCase()
                    )
                }
                when (gameState) {
                    GameState.TABOO_INFO -> _uiState.update {
                        it.copy(
                            round = 1,
                            text = "Guide your team to guess what’s on the card — just don’t use the forbidden words or anything too obvious"
                        )
                    }

                    GameState.PUNS_INFO -> _uiState.update {
                        it.copy(
                            round = 2,
                            text = "No talking allowed! Act, gesture, or mime to get your team to guess the card."
                        )
                    }

                    GameState.TABOO_SHORT_INFO -> _uiState.update {
                        it.copy(
                            round = 3,
                            text = "One word, one shot! Say just a single word to describe the card—but no using the word itself or any obvious variations."
                        )
                    }

                    GameState.PUNS_SHORT_INFO -> _uiState.update {
                        it.copy(
                            round = 4,
                            text = "One move, that’s it! Use a single gesture to show what’s on the card—silence only."
                        )
                    }

                    else -> Unit
                }
            }
        }
    }

}
