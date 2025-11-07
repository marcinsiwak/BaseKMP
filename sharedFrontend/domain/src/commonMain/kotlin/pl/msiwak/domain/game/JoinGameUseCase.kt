package pl.msiwak.domain.game

interface JoinGameUseCase {
    suspend operator fun invoke(adminName: String)
}
