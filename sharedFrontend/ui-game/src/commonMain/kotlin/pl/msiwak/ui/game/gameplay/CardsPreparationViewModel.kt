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
import pl.msiwak.common.model.GameState
import pl.msiwak.destination.NavDestination
import pl.msiwak.domain.game.AddCardUseCase
import pl.msiwak.domain.game.GetUserIdUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.navigator.Navigator

class CardsPreparationViewModel(
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    private val addCardUseCase: AddCardUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val navigator: Navigator
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
            is CardsPreparationUiAction.OnAddCardClicked -> viewModelScope.launch {
                addCardUseCase(uiState.value.text)
                _uiState.update { it.copy(text = "") }
            }
            is CardsPreparationUiAction.OnAnimationFinished -> _uiState.update { it.copy(isAnimationPlaying = false) }
        }
    }

    private suspend fun observeGameSession() {
        observeGameSessionUseCase().filterNotNull().collectLatest { gameSession ->
            with(gameSession) {
                _uiState.update {
                    it.copy(
                        cardLimits = Pair(
                            cards.filter { card -> card.playerId == getUserIdUseCase() }.size,
                            cardsPerPlayer
                        ),
                        cards = cards,
                        isAnimationPlaying = true
                    )
                }
//                if (gameState == GameState.TABOO_INFO) {
//                    navigator.navigate(NavDestination.GameDestination.RoundInfoScreen)
//                }
            }
        }
    }
}