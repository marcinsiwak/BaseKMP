package pl.msiwak.common.model

import kotlinx.serialization.Serializable
@Serializable
sealed class WebSocketEvent {

    @Serializable
    sealed class ServerEvents : WebSocketEvent() {
        @Serializable
        data class PlayerConnected(val currentPlayers: List<String>) : ServerEvents()
        @Serializable
        data class PlayerDisconnected(val currentPlayers: List<String>) : ServerEvents()
    }

    @Serializable
    sealed class ClientEvents : WebSocketEvent() {
        @Serializable
        data class PlayerConnected(val player: String) : ClientEvents()
        @Serializable
        data class PlayerDisconnected(val player: String) : ClientEvents()
    }
}
