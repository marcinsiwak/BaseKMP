package pl.msiwak.domainimpl.game

import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.CheckWifiIsOnUseCase

class CheckWifiIsOnUseCaseImpl(private val gameRepository: GameRepository) : CheckWifiIsOnUseCase {
    override suspend fun invoke(): Boolean {
        return gameRepository.checkWifiIsOn()
    }
}
