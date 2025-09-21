package pl.msiwak.domain.game

interface ConnectPlayerToGameUseCase {
    suspend operator fun invoke(playerName: String)
}
