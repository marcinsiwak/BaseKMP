package pl.msiwak.domainimpl.game

import kotlinx.coroutines.flow.StateFlow
import pl.msiwak.common.model.GameSession
import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.ObserveGameSessionUseCase

class ObserveGameSessionUseCaseImpl(
    private val gameRepository: GameRepository
) : ObserveGameSessionUseCase {
    override suspend fun invoke(): StateFlow<GameSession?> = gameRepository.currentGameSession
}
