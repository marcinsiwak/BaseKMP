package pl.msiwak.ui.game.finish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.domain.game.ContinueGameUseCase
import pl.msiwak.domain.game.GetUserIdUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.domain.game.SetCorrectAnswerUseCase
import pl.msiwak.navigator.Navigator
import pl.msiwak.ui.game.TeamItem

class FinishViewModel(
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val continueGameUseCase: ContinueGameUseCase,
    private val setCorrectAnswerUseCase: SetCorrectAnswerUseCase,
    val navigator: Navigator
) : ViewModel() {

    init {
        viewModelScope.launch {
            observeGameSession()
        }
    }

    private val _uiState = MutableStateFlow(FinishViewState())
    val uiState: StateFlow<FinishViewState> = _uiState.asStateFlow()

    fun onUiAction(action: FinishUiAction) {

    }

    private suspend fun observeGameSession() {
        observeGameSessionUseCase().filterNotNull().collectLatest { gameSession ->
            with(gameSession) {
                _uiState.update {
                    it.copy(
                        teams = teams.map { team ->
                            TeamItem(
                                team.name,
                                players.filter { player -> team.playerIds.contains(player.id) })
                        }
                    )
                }
            }
        }
    }
}
