package pl.msiwak.domainimpl.game

import pl.msiwak.connection.model.WebSocketEvent
import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.SendClientEventUseCase

class SendClientEventUseCaseImpl(
    private val gameRepository: GameRepository
) : SendClientEventUseCase {

    override suspend fun invoke(webSocketEvent: WebSocketEvent) {
        gameRepository.sendClientEvent(webSocketEvent)
    }
}
