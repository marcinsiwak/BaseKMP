package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.ObserveHostIpUseCase

class ObserveHostIpUseCaseImpl(
    private val gameRepository: GameRepository
) : ObserveHostIpUseCase {
    override suspend fun invoke()  {

    }
}
