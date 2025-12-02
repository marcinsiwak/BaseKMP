package pl.msiwak.domainimpl.game

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import pl.msiwak.common.model.WifiState
import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.ObserveIsWifiOnUseCase

class ObserveIsWifiOnUseCaseImpl(private val gameRepository: GameRepository) : ObserveIsWifiOnUseCase {
    override suspend fun invoke(): Flow<WifiState> {
//        return gameRepository.observeWifiState().distinctUntilChanged()
        return flow {  }
    }
}
