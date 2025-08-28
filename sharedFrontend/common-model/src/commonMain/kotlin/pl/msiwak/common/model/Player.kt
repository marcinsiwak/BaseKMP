package pl.msiwak.common.model

data class Player(
    val id: String,
    val name: String,
    val isConnected: Boolean = false,
    val connectionTime: Long = 0L
)
