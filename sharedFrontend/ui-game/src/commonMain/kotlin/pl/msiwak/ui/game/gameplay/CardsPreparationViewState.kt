package pl.msiwak.ui.game.gameplay

data class CardsPreparationViewState(
    val text: String = "",
    val cards: List<String> = emptyList(),
    val isAnimationPlaying: Boolean = false
)