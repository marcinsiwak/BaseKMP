package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.InitServerElectionUseCase

class InitServerElectionUseCaseImpl(private val gameRepository: GameRepository) : InitServerElectionUseCase {
    override fun invoke() {
//        gameRepository.startElection()

    }
}
