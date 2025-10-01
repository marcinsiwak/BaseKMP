package pl.msiwak.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val id: Int,
    val name: String,
    val playerIds: List<String> = emptyList()
)
