package pl.msiwak.ui.game

sealed class LobbyUiAction {
    data object OnBackClicked : LobbyUiAction()
    data object Disconnect : LobbyUiAction()
    data object SetReady : LobbyUiAction()
    data class JoinTeam(val teamName: String) : LobbyUiAction()
}
