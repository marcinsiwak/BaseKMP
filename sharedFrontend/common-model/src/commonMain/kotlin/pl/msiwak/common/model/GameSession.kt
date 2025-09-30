package pl.msiwak.common.model

import kotlinx.serialization.Serializable

@Serializable
data class GameSession(
    val gameId: String,
    val players: List<Player> = emptyList(),
    val isStarted: Boolean = false,
    val adminId: String? = null,
    val gameServerIpAddress: String? = null,
    val cardsPerPlayer: Int = 4,
    val gameState: GameState = GameState.WAITING_FOR_PLAYERS,
    val currentPlayerId: String? = null,
)

@Serializable
enum class GameState {
    WAITING_FOR_PLAYERS,
    PREPARING_CARDS,
    TABOO,
    PUNS,
    TABOO_SHORT,
    PUNS_SHORT,
    FINISHED
}