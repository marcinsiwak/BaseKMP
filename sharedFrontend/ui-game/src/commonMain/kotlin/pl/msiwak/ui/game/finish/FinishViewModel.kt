package pl.msiwak.ui.game.finish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.destination.NavDestination
import pl.msiwak.domain.game.FinishGameUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.domain.game.PlayAgainUseCase
import pl.msiwak.navigator.Navigator
import pl.msiwak.ui.game.TeamItem

class FinishViewModel(
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    private val finishGameUseCase: FinishGameUseCase,
    private val navigator: Navigator,
    private val playAgainUseCase: PlayAgainUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FinishViewState())
    val uiState: StateFlow<FinishViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeGameSession()
            finishGameUseCase()
        }
        viewModelScope.launch {
            startCountdownTimer()
        }
    }

    fun onUiAction(action: FinishUiAction) {
        when (action) {
            is FinishUiAction.OnPlayAgainClicked -> viewModelScope.launch {
                launch {  playAgainUseCase() }
                navigator.navigate(NavDestination.GameDestination.StartScreen)
            }
        }
    }

    private suspend fun observeGameSession() {
        observeGameSessionUseCase().collectLatest { gameSession ->
            with(gameSession) {
                if (this == null) {
                    navigator.navigate(NavDestination.GameDestination.StartScreen)
                } else {
                    _uiState.update {
                        it.copy(
                            teams = teams.map { team ->
                                TeamItem(
                                    team.name,
                                    players.filter { player -> team.playerIds.contains(player.id) },
                                    team.score
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    private fun startCountdownTimer() {
        val timeToPlayAgain = 5

        viewModelScope.launch {
            repeat(timeToPlayAgain) { i ->
                delay(1000)
                val newTimeRemaining = timeToPlayAgain - i - 1
                _uiState.update { it.copy(timeRemaining = newTimeRemaining) }
            }
        }
    }
}
