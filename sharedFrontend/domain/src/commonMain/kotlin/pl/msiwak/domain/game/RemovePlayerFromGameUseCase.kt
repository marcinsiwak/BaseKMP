package pl.msiwak.domain.game

interface RemovePlayerFromGameUseCase {
    suspend operator fun invoke(playerId: String): Boolean
}
