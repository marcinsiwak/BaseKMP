package pl.msiwak.ui.game.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.destination.NavDestination
import pl.msiwak.domain.game.FindGameIPAddressUseCase
import pl.msiwak.domain.game.JoinGameUseCase
import pl.msiwak.navigator.Navigator

class StartViewModel(
    private val joinGameUseCase: JoinGameUseCase,
    private val findGameIPAddressUseCase: FindGameIPAddressUseCase,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(StartState())
    val uiState: StateFlow<StartState> = _uiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { it.copy(isLoading = false) }
    }
    private var findGameJob: Job? = null
    private var joinJob: Job? = null


    fun onUiAction(action: StartUiAction) {
        when (action) {
            is StartUiAction.OnBackClicked -> {
                // Handle navigation back
            }

            is StartUiAction.CreateGame -> Unit
            is StartUiAction.OnPlayerNameChanged -> updatePlayerName(action.name)
            is StartUiAction.JoinGame -> Unit
            is StartUiAction.Refresh -> findGame()
            is StartUiAction.Join -> join()
        }
    }

    private fun join() {
        if (joinJob?.isActive == true) return
        joinJob = viewModelScope.launch(errorHandler) {
            _uiState.update { it.copy(isLoading = true) }
            joinGameUseCase(uiState.value.playerName)
            _uiState.update { it.copy(isLoading = false) }
            navigator.navigate(NavDestination.GameDestination.LobbyScreen)
        }
    }

    private fun findGame() {
        if (findGameJob?.isActive == true) return
        findGameJob = viewModelScope.launch(errorHandler) {
            val existingGameIpAddress = findGameIPAddressUseCase()
            _uiState.update { it.copy(gameIpAddress = existingGameIpAddress) }
        }
    }

    private fun updatePlayerName(name: String) {
        _uiState.update { it.copy(playerName = name) }
    }

}
