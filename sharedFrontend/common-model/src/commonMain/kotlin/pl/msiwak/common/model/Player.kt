package pl.msiwak.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: String,
    val name: String
)
