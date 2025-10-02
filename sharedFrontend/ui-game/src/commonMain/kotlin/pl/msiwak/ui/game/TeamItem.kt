package pl.msiwak.ui.game

import pl.msiwak.common.model.Player

data class TeamItem(
    val name: String,
    val players: List<Player>
)