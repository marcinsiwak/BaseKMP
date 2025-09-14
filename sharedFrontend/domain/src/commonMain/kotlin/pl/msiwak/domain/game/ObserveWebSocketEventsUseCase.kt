package pl.msiwak.domain.game

import kotlinx.coroutines.flow.Flow
import pl.msiwak.common.model.WebSocketEvent

interface ObserveWebSocketEventsUseCase {
    suspend operator fun invoke(): Flow<WebSocketEvent>
}
