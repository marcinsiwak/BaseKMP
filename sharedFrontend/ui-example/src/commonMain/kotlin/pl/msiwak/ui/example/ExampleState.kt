package pl.msiwak.ui.example

import pl.msiwak.common.model.Player

data class ExampleState(
    val players: List<Player> = emptyList()
)
