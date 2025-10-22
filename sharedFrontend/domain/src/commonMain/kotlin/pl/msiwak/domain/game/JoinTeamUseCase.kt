package pl.msiwak.domain.game

interface JoinTeamUseCase {
    suspend operator fun invoke(teamName: String)
}
