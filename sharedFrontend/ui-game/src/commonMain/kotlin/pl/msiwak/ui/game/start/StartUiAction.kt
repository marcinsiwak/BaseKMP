package pl.msiwak.ui.game.start

sealed class StartUiAction {
    data object OnBackClicked : StartUiAction()
    data object CreateGame : StartUiAction()
    data object JoinGame : StartUiAction()
    data class OnPlayerNameChanged(val name: String) : StartUiAction()
    data object Refresh : StartUiAction()
}
