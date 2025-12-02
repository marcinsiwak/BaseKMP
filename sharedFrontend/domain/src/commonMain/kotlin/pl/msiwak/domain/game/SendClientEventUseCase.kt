package pl.msiwak.domain.game

import pl.msiwak.connection.model.WebSocketEvent

interface SendClientEventUseCase {
    suspend operator fun invoke(webSocketEvent: WebSocketEvent)
}
