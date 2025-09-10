package pl.msiwak.domain.game

interface AddPlayerToGameUseCase {
    suspend operator fun invoke(host: String, playerName: String)
}
