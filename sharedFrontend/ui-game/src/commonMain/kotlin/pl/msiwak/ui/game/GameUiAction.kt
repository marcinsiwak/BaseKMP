package pl.msiwak.ui.game

sealed class GameUiAction {
    data object OnBackClicked : GameUiAction()
    data object StartSession : GameUiAction()
    data object StopSession : GameUiAction()
    data object Connect : GameUiAction()
    data object Disconnect : GameUiAction()
    data object Refresh : GameUiAction()
}
