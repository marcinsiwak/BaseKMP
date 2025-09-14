package pl.msiwak.ui.game

sealed class GameUiAction {
    data object OnBackClicked : GameUiAction()
    data object StartGame : GameUiAction()
    data object Disconnect : GameUiAction()
}
