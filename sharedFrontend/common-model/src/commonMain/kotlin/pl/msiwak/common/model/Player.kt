package pl.msiwak.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: String,
    val name: String,
    val isActive: Boolean = false,
    val isReady: Boolean = false,
    val cards: List<Card> = emptyList()
)
