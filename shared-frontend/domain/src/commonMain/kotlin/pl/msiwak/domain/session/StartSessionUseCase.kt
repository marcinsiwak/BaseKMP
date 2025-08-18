package pl.msiwak.domain.session

interface StartSessionUseCase {
    suspend operator fun invoke()
}
