package pl.msiwak.domainimpl.game

import kotlinx.coroutines.flow.Flow
import pl.msiwak.common.model.WebSocketEvent
import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.ObservePlayersConnectionUseCase

class ObservePlayersConnectionUseCaseImpl(
    private val gameRepository: GameRepository
) : ObservePlayersConnectionUseCase {
    override suspend fun invoke(): Flow<WebSocketEvent.ServerEvents> = gameRepository.observePlayersConnection()
}
