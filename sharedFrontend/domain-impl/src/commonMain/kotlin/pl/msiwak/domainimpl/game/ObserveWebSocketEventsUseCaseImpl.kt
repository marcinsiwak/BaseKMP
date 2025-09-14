package pl.msiwak.domainimpl.game

import kotlinx.coroutines.flow.Flow
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.ObserveWebSocketEventsUseCase

class ObserveWebSocketEventsUseCaseImpl(
    private val gameRepository: GameRepository
) : ObserveWebSocketEventsUseCase {
    override suspend fun invoke(): Flow<WebSocketEvent> = gameRepository.observePlayersConnection()
}
