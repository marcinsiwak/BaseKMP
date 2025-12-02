package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.DisconnectUseCase

class DisconnectUseCaseImpl(private val gameRepository: GameRepository) : DisconnectUseCase {
    override suspend fun invoke() {
//        gameRepository.disconnectPlayer()
    }
}
