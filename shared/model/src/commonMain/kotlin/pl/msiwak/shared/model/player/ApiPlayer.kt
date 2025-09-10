package pl.msiwak.shared.model.player

import kotlinx.serialization.Serializable

@Serializable
class ApiPlayer(
    val id: String,
    val name: String
)
