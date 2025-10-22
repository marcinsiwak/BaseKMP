package pl.msiwak.ui.game.gameplay

import pl.msiwak.common.model.Card

data class CardsPreparationViewState(
    val text: String = "",
    val cards: List<Card> = emptyList(),
    val isAnimationPlaying: Boolean = false,
    val cardLimits: Pair<Int, Int> = Pair(0, 0)
)