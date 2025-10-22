package pl.msiwak.ui.game.roundinfo

sealed class RoundInfoUiAction() {
    data object OnStartRoundClick : RoundInfoUiAction()
}