package pl.msiwak.ui.game.round

import pl.msiwak.common.model.Card

data class RoundViewState(
    val text: String = "",
    val currentCard: Card? = null,
    val isPlayerRound: Boolean = false,
    val isRoundFinished: Boolean = false,
    val currentPlayerName: String = ""
)