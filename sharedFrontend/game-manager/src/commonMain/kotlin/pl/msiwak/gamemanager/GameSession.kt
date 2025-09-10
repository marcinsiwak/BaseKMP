package pl.msiwak.gamemanager

import pl.msiwak.common.model.Player

data class GameSession(
    val gameId: String,
    val players: List<Player> = emptyList(),
    val isStarted: Boolean = false
)