package pl.msiwak.connection.model

import kotlinx.serialization.Serializable

@Serializable
sealed class ClientActions : WebSocketEvent() {

    // connection events
    @Serializable
    data class UserConnected(val id: String, val isHost: Boolean) : ClientActions()

    @Serializable
    data class UserDisconnected(val id: String) : ClientActions()

    @Serializable
    data object ServerDownDetected : ClientActions()
}
