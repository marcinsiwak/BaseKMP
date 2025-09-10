package pl.msiwak.ui.game

import pl.msiwak.common.model.Player

data class GameState(
    val isLoading: Boolean = false,
    val players: List<Player> = emptyList(),
    val gameIpAddress: String? = null
)
