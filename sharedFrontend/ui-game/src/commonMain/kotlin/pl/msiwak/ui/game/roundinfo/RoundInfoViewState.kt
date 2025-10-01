package pl.msiwak.ui.game.roundinfo

data class RoundInfoViewState(
    val round: Int = 0,
    val text: String = "",
    val currentPlayerName: String = "",
    val isCurrentPlayerRound: Boolean = false
)