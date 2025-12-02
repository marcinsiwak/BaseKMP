package pl.msiwak.ui.game.start

sealed class StartUiAction {
    data object OnBackClicked : StartUiAction()
    data object Join : StartUiAction()
    data class OnPlayerNameChanged(val name: String) : StartUiAction()
    data object DismissDialog : StartUiAction()
}
