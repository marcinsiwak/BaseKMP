package pl.msiwak.domain.game

import pl.msiwak.common.model.WebSocketEvent

interface SendClientEventUseCase {
    suspend operator fun invoke(webSocketEvent: WebSocketEvent)
}
