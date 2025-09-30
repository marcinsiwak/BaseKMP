package pl.msiwak.ui.game.round

data class RoundViewState(
    val text: String = "",
    val currentCard: String = "",
    val isCurrentPlayerRound: Boolean = false
)