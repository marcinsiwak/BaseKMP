package pl.msiwak.domain.player

interface DisconnectPlayerUseCase {
    suspend operator fun invoke(playerId: String): Boolean
}
