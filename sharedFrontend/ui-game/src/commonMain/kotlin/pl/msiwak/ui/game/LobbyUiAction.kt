package pl.msiwak.ui.game

sealed class LobbyUiAction {
    data object OnBackClicked : LobbyUiAction()
    data object Disconnect : LobbyUiAction()
}
