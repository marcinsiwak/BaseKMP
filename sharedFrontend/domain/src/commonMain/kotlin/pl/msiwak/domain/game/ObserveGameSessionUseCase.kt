package pl.msiwak.domain.game

import kotlinx.coroutines.flow.StateFlow
import pl.msiwak.common.model.GameSession

interface ObserveGameSessionUseCase {
    suspend operator fun invoke(): StateFlow<GameSession?>
}
