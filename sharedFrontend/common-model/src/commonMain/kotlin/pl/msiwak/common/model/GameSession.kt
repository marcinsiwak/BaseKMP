package pl.msiwak.common.model

import kotlinx.serialization.Serializable

@Serializable
data class GameSession(
    val gameId: String,
    val players: List<Player> = emptyList(),
    val isStarted: Boolean = false,
    val adminId: String? = null,
    val gameServerIpAddress: String? = null,
    val cardsPerPlayer: Int = 1,
    val gameState: GameState = GameState.WAITING_FOR_PLAYERS,
    val currentPlayerId: String? = null
)

@Serializable
enum class GameState {
    WAITING_FOR_PLAYERS,
    PREPARING_CARDS,
    TABOO_INFO,
    TABOO,
    PUNS_INFO,
    PUNS,
    TABOO_SHORT_INFO,
    TABOO_SHORT,
    PUNS_SHORT_INFO,
    PUNS_SHORT,
    FINISHED
}