package pl.msiwak.domain.game

import kotlinx.coroutines.flow.Flow
import pl.msiwak.common.model.WebSocketEvent

interface ObservePlayersConnectionUseCase {
    suspend operator fun invoke(): Flow<WebSocketEvent>
}
