package pl.msiwak.ui.game.round

import pl.msiwak.common.model.Card

data class RoundViewState(
    val round: Int = 0,
    val currentCard: Card? = null,
    val isPlayerRound: Boolean = false,
    val isRoundFinished: Boolean = false,
    val currentPlayerName: String = "",
    val timeRemaining: Int = 0,
    val isTimerRunning: Boolean = false
)