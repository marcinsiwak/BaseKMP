package pl.msiwak.gamemanager

import kotlinx.coroutines.flow.StateFlow
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.Player

interface GameManager {
    val currentGameSession: StateFlow<GameSession?>

    suspend fun createGame(adminId: String, ipAddress: String?, gameSession: GameSession?)
    suspend fun joinGame(playerId: String)
    suspend fun leaveGame(playerId: String)
    suspend fun disablePlayer(playerId: String)
    suspend fun startGame(gameId: String)
    suspend fun nextRound(gameId: String)
    suspend fun finishGame(gameId: String)
    suspend fun pauseGame(gameId: String)
    suspend fun resumeGame(gameId: String)
    suspend fun getGameSession(): GameSession?

    suspend fun updateAdminId(id: String)

    suspend fun setPlayerReady(id: String)
    suspend fun addCardToGame(userId: String, cardText: String)
    suspend fun continueGame()
    suspend fun joinTeam(userId: String, teamName: String)
    suspend fun setCorrectAnswer(cardText: String)
    suspend fun addPlayerName(userId: String, name: String)
}
