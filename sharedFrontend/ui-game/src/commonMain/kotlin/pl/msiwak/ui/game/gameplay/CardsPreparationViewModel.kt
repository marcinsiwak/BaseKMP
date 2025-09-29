package pl.msiwak.ui.game.gameplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.domain.game.AddCardUseCase
import pl.msiwak.domain.game.GetUserIdUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase

class CardsPreparationViewModel(
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    private val addCardUseCase: AddCardUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardsPreparationViewState())
    val uiState: StateFlow<CardsPreparationViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeGameSession()
        }
    }

    fun onUiAction(action: CardsPreparationUiAction) {
        when (action) {
            is CardsPreparationUiAction.OnTextInput -> _uiState.update { it.copy(text = action.text) }
            is CardsPreparationUiAction.OnAddCardClicked -> viewModelScope.launch { addCardUseCase(uiState.value.text) }
            is CardsPreparationUiAction.OnAnimationFinished -> _uiState.update { it.copy(isAnimationPlaying = false) }
        }
    }

    private suspend fun observeGameSession() {
        observeGameSessionUseCase().filterNotNull().collectLatest { gameSession ->
            with(gameSession) {
                _uiState.update {
                    it.copy(
                        cardLimits = Pair(
                            players.find { player -> player.id == getUserIdUseCase() }?.cards?.count() ?: 0,
                            cardsPerPlayer
                        ),
                        cards = players.map { player -> player.cards }.flatten(),
                        isAnimationPlaying = true
                    )
                }
            }
        }
    }
}