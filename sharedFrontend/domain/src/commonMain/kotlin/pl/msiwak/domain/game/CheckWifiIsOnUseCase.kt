package pl.msiwak.domain.game

interface CheckWifiIsOnUseCase {
    operator suspend fun invoke(): Boolean
}
