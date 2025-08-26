package pl.msiwak.ui.game

data class GameState(
    val isLoading: Boolean = false,
    val players: List<String> = emptyList(),
    val gameIpAddress: String? = null
)
