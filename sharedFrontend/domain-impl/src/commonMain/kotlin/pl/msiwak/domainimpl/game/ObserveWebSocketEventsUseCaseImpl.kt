package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.ObserveWebSocketEventsUseCase

class ObserveWebSocketEventsUseCaseImpl(
    private val gameRepository: GameRepository
) : ObserveWebSocketEventsUseCase {
    override suspend fun invoke() {
//        gameRepository.observeWebSocketEvents()
    }
}
