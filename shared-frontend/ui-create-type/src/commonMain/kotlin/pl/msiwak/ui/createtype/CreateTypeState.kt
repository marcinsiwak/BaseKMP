package pl.msiwak.ui.createtype

import pl.msiwak.common.model.Player

data class CreateTypeState(
    val players: List<Player> = emptyList()
)
