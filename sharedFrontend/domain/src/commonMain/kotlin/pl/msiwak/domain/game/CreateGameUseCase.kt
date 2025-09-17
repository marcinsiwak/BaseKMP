package pl.msiwak.domain.game

interface CreateGameUseCase {
    suspend operator fun invoke(adminName: String)
}
