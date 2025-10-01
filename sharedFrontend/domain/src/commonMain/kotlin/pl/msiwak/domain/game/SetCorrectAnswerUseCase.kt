package pl.msiwak.domain.game

interface SetCorrectAnswerUseCase {
    suspend operator fun invoke(cardText: String)
}
