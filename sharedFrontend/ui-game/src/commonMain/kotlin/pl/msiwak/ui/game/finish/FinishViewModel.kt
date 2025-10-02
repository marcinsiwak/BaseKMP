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
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.ui.game.TeamItem

class FinishViewModel(
    private val observeGameSessionUseCase: ObserveGameSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FinishViewState())
    val uiState: StateFlow<FinishViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeGameSession()
        }
    }

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
