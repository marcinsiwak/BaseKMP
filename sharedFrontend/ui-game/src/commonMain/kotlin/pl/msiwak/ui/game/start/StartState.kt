package pl.msiwak.ui.game.start

data class StartState(
    val isLoading: Boolean = false,
    val playerName: String = "Admin",
    val gameIpAddress: String? = null,
)
