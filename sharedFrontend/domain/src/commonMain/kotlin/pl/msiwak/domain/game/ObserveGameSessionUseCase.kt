package pl.msiwak.domain.game

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import pl.msiwak.common.model.GameSession

interface ObserveGameSessionUseCase {
    suspend operator fun invoke(): Flow<GameSession?>
}
