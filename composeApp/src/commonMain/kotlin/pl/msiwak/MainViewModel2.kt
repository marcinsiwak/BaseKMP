package pl.msiwak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import pl.msiwak.common.model.GameActions
import pl.msiwak.common.model.GameState
import pl.msiwak.common.model.ServerGameActions
import pl.msiwak.connection.di.MyConnectionDI
import pl.msiwak.destination.NavDestination
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.globalloadermanager.GlobalLoaderManager
import pl.msiwak.globalloadermanager.GlobalLoaderMessageType
import pl.msiwak.navigator.Navigator

class MainViewModel2(
    private val remoteConfig: RemoteConfig,
    val navigator: Navigator,
    private val observeGameSessionUseCase: ObserveGameSessionUseCase,
    private val globalLoaderManager: GlobalLoaderManager
) : ViewModel() {

    private val _viewState = MutableStateFlow(MainState())
    val viewState: StateFlow<MainState> = _viewState.asStateFlow()
    private val myConnection = MyConnectionDI.getMyConnection()

    init {
        myConnection.setCustomEvents(
            listOf(
                Pair(GameActions.SetPlayerReady::class, GameActions.SetPlayerReady.serializer()),
                Pair(GameActions.JoinTeam::class, GameActions.JoinTeam.serializer()),
                Pair(GameActions.AddCard::class, GameActions.AddCard.serializer()),
                Pair(GameActions.SetCorrectAnswer::class, GameActions.SetCorrectAnswer.serializer()),
                Pair(GameActions.ContinueGame::class, GameActions.ContinueGame.serializer()),
                Pair(GameActions.AddPlayerName::class, GameActions.AddPlayerName.serializer()),
                Pair(ServerGameActions.UpdateGameSession::class, ServerGameActions.UpdateGameSession.serializer())
            )
        )
        viewModelScope.launch {
            launch { remoteConfig.fetch() }
            launch { observeGameSession() }
            launch {
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
            launch {
                myConnection.isWifiConnected.collectLatest { isConnected ->
                    _viewState.update { it.copy(isWifiDialogVisible = !isConnected) }
                }
            }
        }
    }

    fun onUIAction(action: MainAction) {
        when (action) {
            MainAction.OnDialogDismiss -> _viewState.update { it.copy(isWifiDialogVisible = false) }
            MainAction.OnLocalIPErrorDialogConfirm -> {
                _viewState.update { it.copy(localIpIssueError = false) }
            }

            MainAction.OnLocalIPErrorDialogDismiss -> _viewState.update { it.copy(localIpIssueError = false) }
            else -> Unit
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
