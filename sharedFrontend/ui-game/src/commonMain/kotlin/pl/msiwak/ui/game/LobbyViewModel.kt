package pl.msiwak.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.domain.game.DisconnectUseCase
import pl.msiwak.domain.game.GetUserIdUseCase
import pl.msiwak.domain.game.JoinTeamUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.domain.game.SetPlayerReadyUseCase
import pl.msiwak.navigator.Navigator

class LobbyViewModel(
    private val disconnectUseCase: DisconnectUseCase,
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    private val navigator: Navigator,
    private val setPlayerReadyUseCase: SetPlayerReadyUseCase,
    private val joinTeamUseCase: JoinTeamUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyState())
    val uiState: StateFlow<LobbyState> = _uiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        print(throwable)
    }

    init {
        viewModelScope.launch(errorHandler) {
            launch { observeGameSession() }
        }
    }

    fun onUiAction(action: LobbyUiAction) {
        when (action) {
            is LobbyUiAction.OnBackClicked -> {
                // Handle back navigation
            }

            is LobbyUiAction.Disconnect -> viewModelScope.launch(errorHandler) {
                disconnectUseCase()
                navigator.navigateUp()
            }

            LobbyUiAction.SetReady -> viewModelScope.launch(errorHandler) {
                setPlayerReadyUseCase()
                _uiState.update { it.copy(isReady = !it.isReady) }
            }

            is LobbyUiAction.JoinTeam -> viewModelScope.launch {
                joinTeamUseCase(action.teamName)
            }
        }
    }

    private suspend fun observeGameSession() {
        observeGameSessionUseCase().filterNotNull().collectLatest { gameSession ->
            with(gameSession) {
                _uiState.update {
                    it.copy(
                        playersWithoutTeam = players.filter { player ->
                            teams.none { team ->
                                team.playerIds.contains(
                                    player.id
                                )
                            }
                        },
                        gameIpAddress = gameServerIpAddress,
                        teams = teams.map { team ->
                            TeamItem(
                                team.name,
                                players.filter { player -> team.playerIds.contains(player.id) }
                            )
                        },
                        isButtonEnabled = teams.any { team ->  team.playerIds.contains(getUserIdUseCase()) }
                    )
                }
            }
        }
    }
}
