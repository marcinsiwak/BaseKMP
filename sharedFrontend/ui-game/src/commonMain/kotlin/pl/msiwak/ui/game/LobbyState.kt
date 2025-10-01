package pl.msiwak.ui.game

import pl.msiwak.common.model.Player
import pl.msiwak.common.model.Team

data class LobbyState(
    val isLoading: Boolean = false,
    val players: List<Player> = emptyList(),
    val gameIpAddress: String? = null,
    val teams: List<Team> = emptyList()
)
