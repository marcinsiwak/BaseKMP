package pl.msiwak.shared.model.player

import kotlinx.serialization.Serializable

@Serializable
class ApiPlayersResponse(
    val players: List<ApiPlayer>
)
