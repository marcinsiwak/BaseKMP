package pl.msiwak.domain.game

interface AddCardUseCase {
    suspend operator fun invoke(cardText: String)
}
