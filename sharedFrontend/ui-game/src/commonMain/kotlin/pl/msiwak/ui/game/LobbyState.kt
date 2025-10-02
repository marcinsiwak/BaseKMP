package pl.msiwak.ui.game

import pl.msiwak.common.model.Player

data class LobbyState(
    val isLoading: Boolean = false,
    val playersWithoutTeam: List<Player> = emptyList(),
    val gameIpAddress: String? = null,
    val teams: List<TeamItem> = emptyList()
)
