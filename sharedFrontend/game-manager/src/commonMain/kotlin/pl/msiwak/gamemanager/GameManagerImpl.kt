package pl.msiwak.gamemanager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import pl.msiwak.cardsthegame.remoteconfig.RemoteConfig
import pl.msiwak.common.model.Card
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.GameState
import pl.msiwak.common.model.Player
import pl.msiwak.common.model.dispatcher.Dispatchers
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
class GameManagerImpl(
    private val remoteConfig: RemoteConfig
) : GameManager {
    private val _currentGameSession = MutableStateFlow<GameSession?>(null)
    override val currentGameSession: StateFlow<GameSession?> = _currentGameSession.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        scope.launch {
            currentGameSession.collect { gameSession ->
                if (gameSession?.gameState == GameState.SUMMARY) {
                    delay(1000)
                    _currentGameSession.update { it?.copy(gameState = GameState.FINISHED) }
                    delay(1000)
                    with(currentGameSession.value) {
                        _currentGameSession.value = GameSession(
                            gameId = Uuid.random().toString(),
                            adminId = this?.adminId,
                            gameServerIpAddress = this?.gameServerIpAddress
                        )
                    }
                }
            }
        }
    }

    override suspend fun createGame(adminId: String, ipAddress: String?, gameSession: GameSession?) {
        val time = Clock.System.now().toEpochMilliseconds()
        if (gameSession != null) {
            _currentGameSession.value = gameSession.copy(
                adminId = adminId,
                gameServerIpAddress = ipAddress,
                players = gameSession.players.map { if (it.id != ipAddress) it.copy(isActive = false) else it },
                lastUpdateTimestamp = time
            )
        } else {
            _currentGameSession.value =
                GameSession(
                    gameId = Uuid.random().toString(),
                    adminId = adminId,
                    gameServerIpAddress = ipAddress,
                    lastUpdateTimestamp = time
                )
        }
    }

    override suspend fun joinGame(playerId: String) {
        val time = Clock.System.now().toEpochMilliseconds()
        val newPlayer = Player(playerId, isActive = true)

        with(currentGameSession.value ?: throw IllegalStateException("Game has not been created yet")) {
            if (players.any { it.id == playerId }) {
                val updatedPlayers = players.map { if (it.id == playerId) it.copy(isActive = true) else it }
                _currentGameSession.update { it?.copy(players = updatedPlayers, lastUpdateTimestamp = time) }
            } else {
                _currentGameSession.update { it?.copy(players = players + newPlayer, lastUpdateTimestamp = time) }
            }
        }
        println("OUTPUT: Players joined: ${currentGameSession.value!!.players}")

    }

    override suspend fun addPlayerName(userId: String, name: String) {
        val time = Clock.System.now().toEpochMilliseconds()

        with(currentGameSession.value ?: throw IllegalStateException("Game has not been created yet")) {
            val updatedPlayers = players.map { if (it.id == userId) it.copy(name = name) else it }
            _currentGameSession.update { it?.copy(players = updatedPlayers, lastUpdateTimestamp = time) }
        }
    }

    override suspend fun leaveGame(playerId: String) {
        val time = Clock.System.now().toEpochMilliseconds()

        _currentGameSession.update {
            it?.copy(players = it.players.filter { player -> player.id != playerId }, lastUpdateTimestamp = time)
        }
    }

    override suspend fun disablePlayer(playerId: String) {
        val time = Clock.System.now().toEpochMilliseconds()

        _currentGameSession.update {
            it?.copy(
                players = it.players.map { player ->
                    if (player.id == playerId) {
                        player.copy(isActive = false)
                    } else {
                        player
                    }
                },
                lastUpdateTimestamp = time
            )
        }
    }

    override suspend fun startGame(gameId: String) {
        val time = Clock.System.now().toEpochMilliseconds()

        _currentGameSession.update { it?.copy(isStarted = true, lastUpdateTimestamp = time) }
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
        val time = Clock.System.now().toEpochMilliseconds()

        _currentGameSession.update { it?.copy(adminId = id, lastUpdateTimestamp = time) }
    }

    override suspend fun setPlayerReady(id: String) {
        val time = Clock.System.now().toEpochMilliseconds()

        val updatedPlayers = currentGameSession.value?.players?.map { player ->
            if (player.id == id) {
                player.copy(isReady = !player.isReady)
            } else {
                player
            }
        } ?: emptyList()
        val minPlayers = remoteConfig.getMinPlayers()
        val counts = currentGameSession.value?.teams?.map { it.playerIds.size }
        val min = counts?.min() ?: 0
        val max = counts?.max() ?: 0
        _currentGameSession.update {
            it?.copy(
                players = updatedPlayers,
                gameState = if (
                    updatedPlayers.size >= minPlayers &&
                    updatedPlayers.all { player -> player.isReady } &&
                    max - min <= 1
                ) {
                    GameState.PREPARING_CARDS
                } else {
                    it.gameState
                },
                lastUpdateTimestamp = time
            )
        }
    }

    override suspend fun addCardToGame(userId: String, cardText: String) {
        val time = Clock.System.now().toEpochMilliseconds()

        val gameSession = currentGameSession.value ?: return
        val playerCards = gameSession.cards.filter { it.playerId == userId }
        val cardsPerPlayerLimit = gameSession.cardsPerPlayer
        if (playerCards.size == cardsPerPlayerLimit) return

        val updatedCards = gameSession.cards + Card(text = cardText, playerId = userId)

        _currentGameSession.update {
            if (updatedCards.size == gameSession.cardsPerPlayer * gameSession.players.count()) {
                it?.copy(
                    players = gameSession.players,
                    gameState = GameState.TABOO_INFO,
                    currentPlayerId = gameSession.teams.first().playerIds.first(),
                    cards = updatedCards,
                    lastUpdateTimestamp = time
                )
            } else {
                it?.copy(players = gameSession.players, cards = updatedCards, lastUpdateTimestamp = time)
            }
        }
    }

    override suspend fun continueGame() {
        val time = Clock.System.now().toEpochMilliseconds()
        val gameSession = currentGameSession.value ?: return
        val currentGameState = gameSession.gameState
        when (currentGameState) {
            GameState.TABOO_INFO -> _currentGameSession.update {
                it?.copy(
                    gameState = GameState.TABOO,
                    currentRoundStartDate = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    lastUpdateTimestamp = time
                )
            }

            GameState.PUNS_INFO -> _currentGameSession.update {
                it?.copy(
                    gameState = GameState.PUNS,
                    currentRoundStartDate = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    lastUpdateTimestamp = time
                )
            }

            GameState.TABOO_SHORT_INFO -> _currentGameSession.update {
                it?.copy(
                    gameState = GameState.TABOO_SHORT,
                    currentRoundStartDate = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    lastUpdateTimestamp = time
                )
            }

            GameState.PUNS_SHORT_INFO -> _currentGameSession.update {
                it?.copy(
                    gameState = GameState.PUNS_SHORT,
                    currentRoundStartDate = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    lastUpdateTimestamp = time
                )
            }

            GameState.TABOO -> handleGameContinuation(GameState.PUNS_INFO, GameState.TABOO_INFO)
            GameState.PUNS -> handleGameContinuation(GameState.TABOO_SHORT_INFO, GameState.PUNS_INFO)
            GameState.TABOO_SHORT -> handleGameContinuation(
                GameState.PUNS_SHORT_INFO,
                GameState.TABOO_SHORT_INFO
            )

            GameState.PUNS_SHORT -> handleGameContinuation(GameState.SUMMARY, GameState.PUNS_SHORT_INFO)

            else -> Unit
//            GameState.PREPARING_CARDS -> TODO()
//            GameState.WAITING_FOR_PLAYERS -> TODO()
//            GameState.FINISHED -> TODO()
        }
    }

    private fun handleGameContinuation(nextGameState: GameState, fallbackGameState: GameState) {
        val time = Clock.System.now().toEpochMilliseconds()

        _currentGameSession.update {
            if (it?.cards?.all { card -> !card.isAvailable } == true) {
                it.copy(
                    currentPlayerId = it.nextPlayer(),
                    cards = it.cards.map { card -> card.copy(isAvailable = true) },
                    gameState = nextGameState,
                    currentRoundStartDate = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    lastUpdateTimestamp = time
                )

            } else {
                it?.copy(
                    currentPlayerId = if (fallbackGameState == GameState.SUMMARY) null else it.nextPlayer(),
                    gameState = fallbackGameState,
                    teams = if (fallbackGameState == GameState.SUMMARY) it.teams.sortedByDescending { team -> team.score } else it.teams,
                    currentRoundStartDate = Clock.System.now().toLocalDateTime(TimeZone.UTC),
                    lastUpdateTimestamp = time
                )
            }
        }
    }

    override suspend fun joinTeam(userId: String, teamName: String) {
        val time = Clock.System.now().toEpochMilliseconds()

        _currentGameSession.update {
            it?.copy(
                teams = it.teams.map { team ->
                    if (team.name == teamName) {
                        if (team.playerIds.contains(userId)) return
                        team.copy(playerIds = team.playerIds.plus(userId))
                    } else {
                        team.copy(playerIds = team.playerIds.filter { playerId -> playerId != userId })
                    }
                },
                lastUpdateTimestamp = time
            )
        }
    }

    override suspend fun setCorrectAnswer(cardText: String) {
        val time = Clock.System.now().toEpochMilliseconds()

        _currentGameSession.update {
            val playerToUpdateScore = it?.players?.find { player -> player.id == it.currentPlayerId }
            val updatedPlayers = it?.players?.map { player ->
                if (player.id == playerToUpdateScore?.id) {
                    player.copy(score = player.score + 1)
                } else {
                    player
                }
            } ?: emptyList()

            val updatedTeams = it?.teams?.map { team ->
                if (team.playerIds.contains(it.currentPlayerId)) {
                    team.copy(score = team.score + 1)
                } else {
                    team
                }
            }?.sortedByDescending { team -> team.score } ?: emptyList()

            it?.copy(
                cards = it.cards.map { card ->
                    if (card.text == cardText) {
                        card.copy(isAvailable = false)
                    } else {
                        card
                    }
                },
                players = updatedPlayers,
                teams = updatedTeams,
                lastUpdateTimestamp = time
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
