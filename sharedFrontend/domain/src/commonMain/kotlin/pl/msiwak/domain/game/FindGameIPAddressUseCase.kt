package pl.msiwak.domain.game

interface FindGameIPAddressUseCase {
    suspend operator fun invoke(): String?
}
