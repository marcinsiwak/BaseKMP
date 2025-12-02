package pl.msiwak.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: String,
    val name: String? = null,
    val isActive: Boolean = false,
    val isReady: Boolean = false,
    val hasPlayedThisRound: Boolean = false,
    val score: Int = 0
)
