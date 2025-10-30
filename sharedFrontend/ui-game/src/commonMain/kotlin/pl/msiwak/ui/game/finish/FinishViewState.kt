package pl.msiwak.ui.game.finish

import pl.msiwak.ui.game.TeamItem

data class FinishViewState(
    val teams: List<TeamItem>? = null,
    val timeRemaining: Int = 0
)