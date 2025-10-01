package pl.msiwak.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val text: String,
    val isAvailable: Boolean = true,
    val playerId: String
)
