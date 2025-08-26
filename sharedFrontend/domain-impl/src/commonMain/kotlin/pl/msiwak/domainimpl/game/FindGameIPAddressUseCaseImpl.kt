package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.FindGameIPAddressUseCase

class FindGameIPAddressUseCaseImpl(private val gameRepository: GameRepository) : FindGameIPAddressUseCase {
    override suspend fun invoke(): String? {
        return gameRepository.findGame()
    }
}
