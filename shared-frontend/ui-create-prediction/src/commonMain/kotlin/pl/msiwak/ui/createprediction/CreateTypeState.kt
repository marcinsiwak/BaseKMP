package pl.msiwak.ui.createprediction

import pl.msiwak.common.model.Player

data class CreateTypeState(
    val players: List<Player> = emptyList()
)
