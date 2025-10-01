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
        val playerCards = gameSession.cards.filter { it.playerId == userId }
        val cardsPerPlayerLimit = gameSession.cardsPerPlayer
        if (playerCards.size == cardsPerPlayerLimit) return

//        val updatedPlayers = gameSession.players.map { player ->
//            if (player.id == userId) {
//                player.copy(cards = player.cards + Card(text = cardText))
//            } else {
//                player
//            }
//        }
        val updatedCards = gameSession.cards + Card(text = cardText, playerId = userId)

        _currentGameSession.update {
            if (updatedCards.size == gameSession.cardsPerPlayer * gameSession.players.count()) {
                it?.copy(
                    players = gameSession.players,
                    gameState = GameState.TABOO_INFO,
                    currentPlayerId = gameSession.teams.first().playerIds.first(),
                    cards = updatedCards
                )
            } else {
                it?.copy(players = gameSession.players, cards = updatedCards)
            }
        }
    }

    override suspend fun continueGame() {
        val gameSession = currentGameSession.value ?: return
        val currentGameState = gameSession.gameState
        when (currentGameState) {
            GameState.TABOO_INFO -> _currentGameSession.update { it?.copy(gameState = (GameState.TABOO)) }
            GameState.PUNS_INFO -> _currentGameSession.update { it?.copy(gameState = (GameState.PUNS)) }
            GameState.TABOO_SHORT_INFO -> _currentGameSession.update { it?.copy(gameState = (GameState.TABOO_SHORT)) }
            GameState.PUNS_SHORT_INFO -> _currentGameSession.update { it?.copy(gameState = (GameState.PUNS_SHORT)) }
            GameState.TABOO -> handleCardAvailabilityTransition(GameState.PUNS_INFO, GameState.TABOO_INFO)
            GameState.PUNS -> handleCardAvailabilityTransition(GameState.TABOO_SHORT_INFO, GameState.PUNS_INFO)
            GameState.TABOO_SHORT -> handleCardAvailabilityTransition(
                GameState.PUNS_SHORT_INFO,
                GameState.TABOO_SHORT_INFO
            )

            GameState.PUNS_SHORT -> handleCardAvailabilityTransition(GameState.FINISHED, GameState.PUNS_SHORT_INFO)

            GameState.PREPARING_CARDS -> TODO()
            GameState.WAITING_FOR_PLAYERS -> TODO()
            GameState.FINISHED -> TODO()
        }
    }

    private fun handleCardAvailabilityTransition(nextGameState: GameState, fallbackGameState: GameState) {
        _currentGameSession.update {
            if (it?.cards?.all { card -> !card.isAvailable } == true) {
                it.copy(
                    currentPlayerId = it.nextPlayer(),
                    cards = it.cards.map { card -> card.copy(isAvailable = true) },
                    gameState = nextGameState,
                )

            } else {
                it?.copy(gameState = fallbackGameState)
            }
        }
    }

    override suspend fun joinTeam(userId: String, teamName: String) {
        _currentGameSession.update {
            it?.copy(
                teams = it.teams.map { team ->
                    if (team.name == teamName) {
                        team.copy(playerIds = team.playerIds + userId)
                    } else {
                        team.copy(playerIds = team.playerIds.filter { playerId -> playerId != userId })
                    }
                }
            )
        }
    }

    override suspend fun setCorrectAnswer(cardText: String) {
        _currentGameSession.update {
            it?.copy(
                cards = it.cards.map { card ->
                    if (card.text == cardText) {
                        card.copy(isAvailable = false)
                    } else {
                        card
                    }
                }
            )
        }
    }

    private fun GameSession.nextPlayer(): String? {
        if (teams.size < 2) return null

        val teamA = teams[0]
        val teamB = teams[1]

        val currentTeam = when (currentPlayerId) {
            null -> teamA
            in teamA.playerIds -> teamB
            in teamB.playerIds -> teamA
            else -> teamA
        }

        val next =
            currentTeam.playerIds.firstNotNullOfOrNull { id -> players.find { it.id == id && !it.hasPlayedThisRound } }

        if (next != null) {
            return next.id
        }

        val resetPlayers = players.map { it.copy(hasPlayedThisRound = false) }
        val resetSession = copy(players = resetPlayers)

        return resetSession.nextPlayer()
    }
}
