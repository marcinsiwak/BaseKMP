package pl.msiwak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.cardsthegame.remoteconfig.RemoteConfig
import pl.msiwak.common.model.GameState
import pl.msiwak.destination.NavDestination
import pl.msiwak.domain.game.ElectServerHostUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.domain.game.ObserveHostIpUseCase
import pl.msiwak.domain.game.ObserveWebSocketEventsUseCase
import pl.msiwak.globalloadermanager.GlobalLoaderManager
import pl.msiwak.navigator.Navigator

class MainViewModel(
    val navigator: Navigator,
    private val observeWebSocketEventsUseCase: ObserveWebSocketEventsUseCase,
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    private val electServerHostUseCase: ElectServerHostUseCase,
    private val observeHostIpUseCase: ObserveHostIpUseCase,
    private val globalLoaderManager: GlobalLoaderManager,
    private val remoteConfig: RemoteConfig
) : ViewModel() {

    private val _viewState = MutableStateFlow(MainState())
    val viewState: StateFlow<MainState> = _viewState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        print(throwable)
    }

    init {
        with(viewModelScope) {
            launch { remoteConfig.fetch() }
            launch(errorHandler) { electServerHostUseCase() }
            launch { observeHostIpUseCase() }
            launch { observeWebSocketEventsUseCase() }
            launch { observeGameSession() }
            launch {
                globalLoaderManager.isLoading.collectLatest { isLoading ->
                    _viewState.update { it.copy(isLoading = isLoading) }
                }
            }
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

