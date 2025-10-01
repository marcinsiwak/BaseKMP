package pl.msiwak.ui.game.round

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.common.model.Card
import pl.msiwak.common.model.GameState
import pl.msiwak.domain.game.ContinueGameUseCase
import pl.msiwak.domain.game.GetUserIdUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.domain.game.SetCorrectAnswerUseCase
import pl.msiwak.navigator.Navigator

class RoundViewModel(
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val continueGameUseCase: ContinueGameUseCase,
    private val setCorrectAnswerUseCase: SetCorrectAnswerUseCase,
    val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoundViewState())
    val uiState: StateFlow<RoundViewState> = _uiState.asStateFlow()

    private var availableCards = listOf<Card>()
    private var currentCard: Card? = null
    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            observeGameSession()
        }
        viewModelScope.launch {
            startCountdownTimer(5)
        }
    }

    fun onUiAction(action: RoundUiAction) {
        when (action) {
            is RoundUiAction.OnCorrectClick -> {
                viewModelScope.launch {
                    currentCard?.text?.let { setCorrectAnswerUseCase(it) }
                    _uiState.update {
                        it.copy(
                            currentCard = getRandomCard()
                        )
                    }
                }
            }

            is RoundUiAction.OnSkipClick -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            currentCard = getRandomCard(),
                        )
                    }
                }
            }

            is RoundUiAction.OnRoundFinished -> {
                viewModelScope.launch {
                    continueGameUseCase()
                }
            }
        }
    }

    private suspend fun observeGameSession() {
        observeGameSessionUseCase().filterNotNull().collectLatest { gameSession ->
            with(gameSession) {
                availableCards = cards.filter { it.isAvailable }
                _uiState.update {
                    it.copy(
                        isPlayerRound = gameSession.currentPlayerId == getUserIdUseCase(),
                        currentCard = getRandomCard(),
                        currentPlayerName = players.find { player -> player.id == currentPlayerId }?.name ?: "",
                        isRoundFinished = availableCards.isEmpty()
                    )
                }
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

    private fun getRandomCard(): Card? {
        return if (availableCards.isNotEmpty()) {
            availableCards.random()
        } else {
            null
        }.also {
            currentCard = it
        }
    }

    private fun startCountdownTimer(durationInSeconds: Int) {
        if (timerJob?.isActive == true) return
        timerJob?.cancel()

        _uiState.update { it.copy(timeRemaining = durationInSeconds, isTimerRunning = true) }

        timerJob = viewModelScope.launch {
            repeat(durationInSeconds) { i ->
                delay(1000)
                _uiState.update { it.copy(timeRemaining = durationInSeconds - i - 1) }
            }
            _uiState.update { it.copy(isTimerRunning = false, timeRemaining = 0) }

        }
    }
}
