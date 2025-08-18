package pl.msiwak.ui.game

sealed class GameUiAction {
    data object OnBackClicked : GameUiAction()
    data object StartSession : GameUiAction()
    data object StopSession : GameUiAction()
}
