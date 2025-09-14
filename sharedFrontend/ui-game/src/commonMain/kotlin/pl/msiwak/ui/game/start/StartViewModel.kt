package pl.msiwak.ui.game.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.domain.game.AddPlayerToGameUseCase
import pl.msiwak.domain.game.CreateGameUseCase
import pl.msiwak.domain.game.FindGameIPAddressUseCase

class StartViewModel(
    private val createGameUseCase: CreateGameUseCase,
    private val addPlayerToGameUseCase: AddPlayerToGameUseCase,
    private val findGameIPAddressUseCase: FindGameIPAddressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StartState())
    val uiState: StateFlow<StartState> = _uiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { it.copy(isLoading = false) }
    }

    init {
        findGame()
    }

    fun onUiAction(action: StartUiAction) {
        when (action) {
            is StartUiAction.OnBackClicked -> {
                // Handle navigation back
            }

            is StartUiAction.CreateGame -> createGame()
            is StartUiAction.OnPlayerNameChanged -> updatePlayerName(action.name)
            is StartUiAction.JoinGame -> joinGame()
            is StartUiAction.Refresh -> findGame()
        }
    }

    private fun findGame() {
        viewModelScope.launch(errorHandler) {
            val existingGameIpAddress = findGameIPAddressUseCase()
            _uiState.update { it.copy(gameIpAddress = existingGameIpAddress) }
        }
    }

    private fun createGame() {
        viewModelScope.launch(errorHandler) {
            _uiState.update { it.copy(isLoading = true) }
            createGameUseCase()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun joinGame() {
        viewModelScope.launch(errorHandler) {
            addPlayerToGameUseCase(
                host = uiState.value.gameIpAddress ?: throw Exception("Game IP address not found"),
                playerName = uiState.value.playerName
            )
        }
    }

    private fun updatePlayerName(name: String) {
        _uiState.update { it.copy(playerName = name) }
    }

}
