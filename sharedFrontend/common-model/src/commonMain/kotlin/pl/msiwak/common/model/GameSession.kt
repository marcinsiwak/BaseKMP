package pl.msiwak.common.model

import kotlinx.serialization.Serializable

@Serializable
data class GameSession(
    val gameId: String,
    val players: List<Player> = emptyList(),
    val isStarted: Boolean = false
)