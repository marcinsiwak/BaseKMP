package pl.msiwak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
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
import pl.msiwak.common.error.LocalIpNotFoundException
import pl.msiwak.common.model.GameState
import pl.msiwak.common.model.WifiState
import pl.msiwak.destination.NavDestination
import pl.msiwak.domain.game.ElectServerHostUseCase
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.domain.game.ObserveHostIpUseCase
import pl.msiwak.domain.game.ObserveIsWifiOnUseCase
import pl.msiwak.domain.game.ObserveWebSocketEventsUseCase
import pl.msiwak.globalloadermanager.GlobalLoaderManager
import pl.msiwak.globalloadermanager.GlobalLoaderMessageType
import pl.msiwak.navigator.Navigator

class MainViewModel(
    val navigator: Navigator,
    private val observeWebSocketEventsUseCase: ObserveWebSocketEventsUseCase,
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    private val electServerHostUseCase: ElectServerHostUseCase,
    private val observeHostIpUseCase: ObserveHostIpUseCase,
    private val globalLoaderManager: GlobalLoaderManager,
    private val remoteConfig: RemoteConfig,
    private val observeIsWifiOnUseCase: ObserveIsWifiOnUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(MainState())
    val viewState: StateFlow<MainState> = _viewState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        print(throwable)
        when (throwable) {
            is LocalIpNotFoundException -> _viewState.update { it.copy(localIpIssueError = true) }
        }
    }

    private var isWifiOn = false
    private var electionJob: Job? = null

    init {
        with(viewModelScope) {
            launch {
                observeIsWifiOnUseCase().collectLatest { wifiState ->
                    println("Wifi state: $isWifiOn")
                    isWifiOn = wifiState == WifiState.CONNECTED
                    _viewState.update { it.copy(isWifiDialogVisible = wifiState == WifiState.DISCONNECTED) }
                    println("Wifi state: $isWifiOn")
                    if (isWifiOn) {
                        initServerElection()
                    }
                }
            }
            launch { observeWebSocketEventsUseCase() }
            launch { observeGameSession() }
            launch(errorHandler) { remoteConfig.fetch() }
            launch(errorHandler) {
                globalLoaderManager.globalLoaderState.filterNotNull().collectLatest { loaderState ->
                    _viewState.update {
                        it.copy(
                            isLoading = loaderState.isLoading,
                            loaderMessage = when (loaderState.messageType) {
                                GlobalLoaderMessageType.MISSING_HOST -> "Missing host"
                                GlobalLoaderMessageType.DEFAULT -> "Loading"
                                else -> null
                            }
                        )
                    }
                }
            }
        }
    }

    private suspend fun initServerElection() {
        electionJob?.cancelAndJoin()
        electionJob = viewModelScope.launch(errorHandler) {
            launch(errorHandler) { observeHostIpUseCase() }
            launch(errorHandler) { electServerHostUseCase() }
        }
    }

    fun onUIAction(action: MainAction) {
        when (action) {
            MainAction.OnDialogConfirm -> {
                viewModelScope.launch {
                    _viewState.update { it.copy(isLoading = true, isWifiDialogVisible = false) }
                    delay(500)
                    if (isWifiOn) {
                        initServerElection()
                        _viewState.update { it.copy(isWifiDialogVisible = false, isLoading = false) }
                    } else {
                        _viewState.update { it.copy(isWifiDialogVisible = true) }
                    }
                }
            }

            MainAction.OnDialogDismiss -> _viewState.update { it.copy(isWifiDialogVisible = false) }
            MainAction.OnLocalIPErrorDialogConfirm -> {
                _viewState.update { it.copy(localIpIssueError = false) }
            }

            MainAction.OnLocalIPErrorDialogDismiss -> _viewState.update { it.copy(localIpIssueError = false) }
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

