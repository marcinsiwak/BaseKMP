package pl.msiwak.common.model

data class Player(
    val number: String,
    val name: String,
    val surname: String,
    val points: String,
    val isPicked: Boolean = false
)
