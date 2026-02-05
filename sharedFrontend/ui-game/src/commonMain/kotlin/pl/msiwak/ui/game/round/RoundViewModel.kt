package pl.msiwak.ui.game.round

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import pl.msiwak.cardsthegame.remoteconfig.RemoteConfig
import pl.msiwak.common.model.Card
import pl.msiwak.common.model.GameState
import pl.msiwak.domain.game.ContinueGameUseCase
import pl.msiwak.domain.game.GetUserIdUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.domain.game.SetCorrectAnswerUseCase
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class RoundViewModel(
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val continueGameUseCase: ContinueGameUseCase,
    private val setCorrectAnswerUseCase: SetCorrectAnswerUseCase,
    private val remoteConfig: RemoteConfig
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoundViewState())
    val uiState: StateFlow<RoundViewState> = _uiState.asStateFlow()

    private var availableCards = listOf<Card>()
    private var currentCard: Card? = null
    private var checkedCards = mutableSetOf<Card>()
    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            observeGameSession()
        }
    }

    fun onUiAction(action: RoundUiAction) {
        when (action) {
            is RoundUiAction.OnCorrectClick -> {
                viewModelScope.launch {
                    currentCard?.text?.let { setCorrectAnswerUseCase(it) }
                    _uiState.update { it.copy(currentCard = getRandomCard()) }
                }
            }

            is RoundUiAction.OnSkipClick -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            currentCard = getRandomCard() ?: it.currentCard,
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
        observeGameSessionUseCase().filterNotNull().collectIndexed { index, gameSession ->
            with(gameSession) {
                availableCards = cards.filter { it.isAvailable }
                _uiState.update {
                    it.copy(
                        isPlayerRound = gameSession.currentPlayerId == getUserIdUseCase(),
                        currentCard = if (index == 0) getRandomCard() ?: getRandomCard() else it.currentCard,
                        currentPlayerName = players.find { player -> player.id == currentPlayerId }?.name ?: "",
                        isRoundFinished = availableCards.isEmpty().also { isEmpty ->
                            if (isEmpty) timerJob?.cancel() else startCountdownTimer(currentRoundStartDate)
                        }
                    )
                }

                when (gameState) {
                    GameState.TABOO -> _uiState.update { it.copy(round = 1) }
                    GameState.PUNS -> _uiState.update { it.copy(round = 2) }
                    GameState.TABOO_SHORT -> _uiState.update { it.copy(round = 3) }
                    GameState.PUNS_SHORT -> _uiState.update { it.copy(round = 4) }
                    else -> Unit
                }
            }
        }
    }

    private fun getRandomCard(): Card? {
        // If all available cards have been checked, reset the checked cards set
        if (checkedCards.size >= availableCards.size - 1 && currentCard != null) {
            checkedCards.clear()
        }

        // Get cards that haven't been checked yet and are not the current card
        val uncheckedCards = availableCards.filter { card ->
            card != currentCard && !checkedCards.contains(card)
        }

        return when {
            uncheckedCards.isNotEmpty() -> {
                uncheckedCards.random().also { newCard ->
                    currentCard?.let { checkedCards.add(it) }
                    currentCard = newCard
                }
            }

            availableCards.isNotEmpty() -> {
                // Fallback: if no unchecked cards available but we have cards, pick any except current
                availableCards.filter { it != currentCard }.randomOrNull()?.also { newCard ->
                    currentCard = newCard
                }
            }

            else -> null

        }.also {
            currentCard = it
        }
    }


    @OptIn(ExperimentalTime::class)
    private fun startCountdownTimer(currentRoundStartDate: LocalDateTime?) {
        if (timerJob?.isActive == true) return

        if (currentRoundStartDate == null) {
            _uiState.update { it.copy(timeRemaining = 0, isTimerRunning = false) }
            return
        }


        val roundDurationSeconds = remoteConfig.getRoundDefaultTime()

        val currentTime = Clock.System.now()
        val startInstant = currentRoundStartDate.toInstant(TimeZone.UTC)
        val elapsedSeconds = (currentTime - startInstant).inWholeSeconds.toInt()
        val remainingSeconds = (roundDurationSeconds - elapsedSeconds).coerceAtLeast(0)

        if (remainingSeconds <= 0) {
            _uiState.update { it.copy(timeRemaining = 0, isTimerRunning = false) }
            return
        }

        _uiState.update { it.copy(timeRemaining = remainingSeconds, isTimerRunning = true) }

        timerJob = viewModelScope.launch {
            runCatching {
                repeat(remainingSeconds) { i ->
                    delay(1000)
                    val newTimeRemaining = remainingSeconds - i - 1
                    _uiState.update { it.copy(timeRemaining = newTimeRemaining) }
                }
                _uiState.update { it.copy(isTimerRunning = false, timeRemaining = 0, isRoundFinished = true) }
            }.onFailure { e ->
                if (e is CancellationException) {
                    _uiState.update { it.copy(isTimerRunning = false, timeRemaining = 0, isRoundFinished = true) }
                    throw e
                }
            }
        }
    }
}
