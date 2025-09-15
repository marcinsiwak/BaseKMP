package pl.msiwak.domainimpl.game

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import pl.msiwak.common.model.GameSession
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.ObserveGameSessionUseCase
import pl.msiwak.domain.game.ObserveWebSocketEventsUseCase

class ObserveGameSessionUseCaseImpl(
    private val gameRepository: GameRepository
) : ObserveGameSessionUseCase {
    override suspend fun invoke(): Flow<GameSession?> = gameRepository.currentGameSession
}
