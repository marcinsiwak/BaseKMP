package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.ElectServerHostUseCase

class ElectServerHostUseCaseImpl(private val gameRepository: GameRepository) : ElectServerHostUseCase {
    override suspend fun invoke() {
//        gameRepository.startElection()
    }
}
