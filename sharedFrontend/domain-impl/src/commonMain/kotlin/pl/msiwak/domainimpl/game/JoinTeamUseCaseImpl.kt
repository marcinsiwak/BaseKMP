package pl.msiwak.domainimpl.game

import pl.msiwak.common.model.GameActions
import pl.msiwak.data.game.GameRepository
import pl.msiwak.domain.game.GetUserIdUseCase
import pl.msiwak.domain.game.JoinTeamUseCase

class JoinTeamUseCaseImpl(
    private val gameRepository: GameRepository,
    private val getUserIdUseCase: GetUserIdUseCase
) : JoinTeamUseCase {

    override suspend fun invoke(teamName: String) {
        gameRepository.sendClientEvent(GameActions.JoinTeam(getUserIdUseCase(), teamName))
    }
}
