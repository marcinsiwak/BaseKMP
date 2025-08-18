package pl.msiwak.domain.session

interface StopSessionUseCase {
    suspend operator fun invoke()
}
