package pl.msiwak.ui.game.round

sealed class RoundUiAction() {
    data object OnCorrectClick : RoundUiAction()
    data object OnSkipClick : RoundUiAction()
}