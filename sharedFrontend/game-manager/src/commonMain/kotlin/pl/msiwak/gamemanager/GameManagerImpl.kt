package pl.msiwak.gamemanager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.Player
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GameManagerImpl : GameManager {
    private val _currentGameSession = MutableStateFlow<GameSession?>(null)
    override val currentGameSession: StateFlow<GameSession?> = _currentGameSession.asStateFlow()

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createGame(maxRounds: Int) {
        _currentGameSession.value = GameSession(gameId = Uuid.random().toString())
    }

    override suspend fun getPlayers(): List<Player> {
        return currentGameSession.value?.players ?: emptyList()
    }

    override suspend fun joinGame(player: Player) {
        _currentGameSession.update { it?.copy(players = it.players + player) }
    }

    override suspend fun leaveGame(playerId: String) {
        _currentGameSession.update {
            it?.copy(players = it.players.filter { player -> player.id != playerId })
        }
    }

    override suspend fun startGame(gameId: String) {
        _currentGameSession.update { it?.copy(isStarted = true) }
    }

    override suspend fun nextRound(gameId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun finishGame(gameId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun pauseGame(gameId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun resumeGame(gameId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getGameSession(): GameSession? {
        return currentGameSession.value
    }
}
