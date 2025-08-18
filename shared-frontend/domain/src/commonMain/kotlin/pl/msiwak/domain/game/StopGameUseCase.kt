package pl.msiwak.domain.game

interface StopGameUseCase {
    suspend operator fun invoke()
}
