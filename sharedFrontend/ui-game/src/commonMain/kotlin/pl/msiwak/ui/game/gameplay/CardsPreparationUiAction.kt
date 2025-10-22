package pl.msiwak.ui.game.gameplay

sealed class CardsPreparationUiAction() {
    data class OnTextInput(val text: String) : CardsPreparationUiAction()
    data object OnAddCardClicked : CardsPreparationUiAction()
    data object OnAnimationFinished : CardsPreparationUiAction()
}