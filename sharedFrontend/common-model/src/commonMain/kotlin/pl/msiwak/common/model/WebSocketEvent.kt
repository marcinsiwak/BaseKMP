package pl.msiwak.common.model

import kotlinx.serialization.Serializable

@Serializable()
sealed class WebSocketEvent() {

    @Serializable
    data class PlayerConnected(val id: String) : WebSocketEvent()

    @Serializable
    data class DisplayCurrentUsers(val currentPlayers: List<String>) : WebSocketEvent()

    @Serializable
    data class PlayerDisconnected(val currentPlayers: List<String>) : WebSocketEvent()

    @Serializable
    data class PlayerClientDisconnected(val id: String) : WebSocketEvent()

    @Serializable
    data object Error : WebSocketEvent()
}
