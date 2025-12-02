package pl.msiwak.domain.game

import kotlinx.coroutines.flow.Flow
import pl.msiwak.common.model.WifiState

interface ObserveIsWifiOnUseCase {
    suspend operator fun invoke(): Flow<WifiState>
}
