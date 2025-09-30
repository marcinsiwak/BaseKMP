package pl.msiwak.gamemanager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pl.msiwak.common.model.Card
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.GameState
import pl.msiwak.common.model.Player
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GameManagerImpl : GameManager {
    private val _currentGameSession = MutableStateFlow<GameSession?>(null)
    override val currentGameSession: StateFlow<GameSession?> = _currentGameSession.asStateFlow()

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createGame(adminId: String, ipAddress: String?, gameSession: GameSession?) {
        if (gameSession != null) {
            _currentGameSession.value = GameSession(
                gameId = gameSession.gameId,
                adminId = adminId,
                gameServerIpAddress = ipAddress,
                players = gameSession.players.map { if (it.id != ipAddress) it.copy(isActive = false) else it },
            )
        } else {
            _currentGameSession.value =
                GameSession(gameId = Uuid.random().toString(), adminId = adminId, gameServerIpAddress = ipAddress)
        }
    }

    override suspend fun getPlayers(): List<Player> {
        return currentGameSession.value?.players ?: emptyList()
    }

    override suspend fun joinGame(player: Player) {
        with(currentGameSession.value ?: throw IllegalStateException("Game has not been created yet")) {
            if (players.any { it.id == player.id }) {
                val updatedPlayers = players.map { if (it.id == player.id) player.copy(isActive = true) else it }
                _currentGameSession.update { it?.copy(players = updatedPlayers) }
            } else {
                _currentGameSession.update { it?.copy(players = players + player) }
            }
        }
    }

    override suspend fun leaveGame(playerId: String) {
        _currentGameSession.update {
            it?.copy(players = it.players.filter { player -> player.id != playerId })
        }
    }

    override suspend fun disablePlayer(playerId: String) {
        _currentGameSession.update {
            it?.copy(players = it.players.map { player ->
                if (player.id == playerId) {
                    player.copy(isActive = false)
                } else {
                    player
                }
            })
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

    override suspend fun updateAdminId(id: String) {
        _currentGameSession.update { it?.copy(adminId = id) }
    }

    override suspend fun setPlayerReady(id: String) {
        val updatedPlayers = currentGameSession.value?.players?.map { player ->
            if (player.id == id) {
                player.copy(isReady = true)
            } else {
                player
            }
        } ?: emptyList()
        _currentGameSession.update {
            it?.copy(
                players = updatedPlayers,
                gameState = if (updatedPlayers.all { player -> player.isReady }) GameState.PREPARING_CARDS else it.gameState
            )
        }
    }

    override suspend fun addCardToGame(userId: String, cardText: String) {
        val gameSession = currentGameSession.value ?: return
        val playerCards = gameSession.players.find { it.id == userId }?.cards ?: emptyList()
        val cardsPerPlayerLimit = gameSession.cardsPerPlayer
        if (playerCards.size == cardsPerPlayerLimit) return

        val updatedPlayers = gameSession.players.map { player ->
            if (player.id == userId) {
                player.copy(cards = player.cards + Card(text = cardText))
            } else {
                player
            }
        }

        _currentGameSession.update {
            if (updatedPlayers.all { player -> player.cards.size == cardsPerPlayerLimit }) {
                it?.copy(
                    players = updatedPlayers,
                    gameState = GameState.TABOO_INFO,
                    currentPlayerId = updatedPlayers.first().id
                )
            } else {
                it?.copy(players = updatedPlayers)
            }
        }
    }

    override suspend fun continueGame() {
        val currentGameState = currentGameSession.value?.gameState ?: return
        when (currentGameState) {
            GameState.TABOO_INFO -> updateGameStateTo(GameState.TABOO)
            GameState.PUNS_INFO -> updateGameStateTo(GameState.PUNS)
            GameState.TABOO_SHORT_INFO -> updateGameStateTo(GameState.TABOO_SHORT)
            GameState.PUNS_SHORT_INFO -> updateGameStateTo(GameState.PUNS_SHORT)
            GameState.TABOO -> updateGameStateTo(GameState.PUNS_INFO)
            GameState.PUNS -> updateGameStateTo(GameState.TABOO_SHORT_INFO)
            GameState.TABOO_SHORT -> updateGameStateTo(GameState.PUNS_SHORT_INFO)
            GameState.PUNS_SHORT -> updateGameStateTo(GameState.FINISHED)

            GameState.PREPARING_CARDS -> TODO()
            GameState.WAITING_FOR_PLAYERS -> TODO()
            GameState.FINISHED -> TODO()
        }
    }

    private fun updateGameStateTo(taboo: GameState) {
        _currentGameSession.update { it?.copy(gameState = taboo) }
    }

}
