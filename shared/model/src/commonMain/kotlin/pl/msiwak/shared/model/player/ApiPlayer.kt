package pl.msiwak.shared.model.player

import kotlinx.serialization.Serializable

@Serializable
class ApiPlayer(
    val id: String,
    val name: String,
    val surname: String,
    val points: String,
    val number: String
)
