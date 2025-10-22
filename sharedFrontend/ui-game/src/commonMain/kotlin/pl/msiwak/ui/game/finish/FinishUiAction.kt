package pl.msiwak.ui.game.finish

sealed class FinishUiAction() {
    data object OnPlayAgainClicked : FinishUiAction()
}