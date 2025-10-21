package pl.msiwak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pl.msiwak.common.model.GameState
import pl.msiwak.destination.NavDestination
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.domain.game.ObserveWebSocketEventsUseCase
import pl.msiwak.navigator.Navigator

class MainViewModel(
    val navigator: Navigator,
    private val observeWebSocketEventsUseCase: ObserveWebSocketEventsUseCase,
    private val observeGameSessionUseCase: ObserveGameSessionUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            observeWebSocketEventsUseCase()
        }
        viewModelScope.launch {
            observeGameSession()
        }
    }

    private suspend fun observeGameSession() {
        observeGameSessionUseCase().filterNotNull().map { it.gameState }.distinctUntilChanged()
            .collectLatest { gameState ->
                when (gameState) {
                    GameState.PREPARING_CARDS -> navigator.navigate(NavDestination.GameDestination.CardsPreparationScreen)
                    GameState.TABOO_INFO -> navigator.navigate(NavDestination.GameDestination.RoundInfoScreen)
                    GameState.TABOO -> navigator.navigate(NavDestination.GameDestination.RoundScreen)
                    GameState.PUNS_INFO -> navigator.navigate(NavDestination.GameDestination.RoundInfoScreen)
                    GameState.PUNS -> navigator.navigate(NavDestination.GameDestination.RoundScreen)
                    GameState.TABOO_SHORT_INFO -> navigator.navigate(NavDestination.GameDestination.RoundInfoScreen)
                    GameState.TABOO_SHORT -> navigator.navigate(NavDestination.GameDestination.RoundScreen)
                    GameState.PUNS_SHORT_INFO -> navigator.navigate(NavDestination.GameDestination.RoundInfoScreen)
                    GameState.PUNS_SHORT -> navigator.navigate(NavDestination.GameDestination.RoundScreen)
                    GameState.SUMMARY -> navigator.navigate(NavDestination.GameDestination.FinishScreen)
                    else -> Unit
                }
            }
    }
}

