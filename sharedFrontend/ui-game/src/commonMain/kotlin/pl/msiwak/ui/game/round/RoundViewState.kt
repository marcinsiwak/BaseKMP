package pl.msiwak.ui.game.round

import pl.msiwak.common.model.Card

data class RoundViewState(
    val text: String = "",
    val currentCard: Card? = null,
    val isCurrentPlayerRound: Boolean = false,
    val isRoundFinished: Boolean = false
)