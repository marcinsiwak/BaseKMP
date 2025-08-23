package pl.msiwak.common.model

import kotlinx.serialization.Serializable
@Serializable
sealed class WebSocketEvent {

    @Serializable
    sealed class PlayerConnection : WebSocketEvent() {
        @Serializable
        data class PlayerConnected(val currentPlayers: List<String>) : PlayerConnection()
        @Serializable
        data class PlayerDisconnected(val currentPlayers: List<String>) : PlayerConnection()
    }
}
