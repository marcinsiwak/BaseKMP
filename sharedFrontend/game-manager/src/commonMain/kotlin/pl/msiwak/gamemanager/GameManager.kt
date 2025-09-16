package pl.msiwak.gamemanager

import kotlinx.coroutines.flow.StateFlow
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.Player

interface GameManager {
    val currentGameSession: StateFlow<GameSession?>

    suspend fun createGame(maxRounds: Int = 10)
    suspend fun joinGame(player: Player)
    suspend fun leaveGame(playerId: String)
    suspend fun disablePlayer(playerId: String)
    suspend fun startGame(gameId: String)
    suspend fun nextRound(gameId: String)
    suspend fun finishGame(gameId: String)
    suspend fun pauseGame(gameId: String)
    suspend fun resumeGame(gameId: String)
    suspend fun getGameSession(): GameSession?

    suspend fun getPlayers(): List<Player>
    suspend fun updateAdminId(id: String)
}
