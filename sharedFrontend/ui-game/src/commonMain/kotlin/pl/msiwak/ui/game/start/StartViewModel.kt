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
import pl.msiwak.domain.game.JoinGameUseCase
import pl.msiwak.navigator.Navigator

class StartViewModel(
    private val joinGameUseCase: JoinGameUseCase,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(StartState())
    val uiState: StateFlow<StartState> = _uiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        println("Exception: $exception")
        _uiState.update { it.copy(isLoading = false, isErrorVisible = true) }
    }
    private var findGameJob: Job? = null
    private var joinJob: Job? = null


    fun onUiAction(action: StartUiAction) {
        when (action) {
            is StartUiAction.OnBackClicked -> {
                // Handle navigation back
            }

            is StartUiAction.OnPlayerNameChanged -> updatePlayerName(action.name)
            is StartUiAction.Join -> join()
            StartUiAction.DismissDialog -> _uiState.update { it.copy(isErrorVisible = false) }
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

    private fun updatePlayerName(name: String) {
        _uiState.update { it.copy(playerName = name) }
    }
}
