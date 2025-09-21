package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.GetUserIdUseCase

class GetUserIdUseCaseImpl(
    private val gameRepository: GameRepository
) : GetUserIdUseCase {

    override fun invoke(): String = gameRepository.getUserId()
}
