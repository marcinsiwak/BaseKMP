package pl.msiwak.common.model

import kotlinx.serialization.Serializable

@Serializable
sealed class WebSocketEvent() {

    @Serializable
    sealed class ClientActions : WebSocketEvent() {
        @Serializable
        data class PlayerConnected(val player: Player) : ClientActions()

        @Serializable
        data class SetPlayerReady(val id: String) : ClientActions()

        @Serializable
        data class PlayerClientDisconnected(val id: String) : ClientActions()

        @Serializable
        data object ServerDownDetected : ClientActions()
    }

    @Serializable
    sealed class ServerActions : WebSocketEvent() {
        @Serializable
        data class UpdateGameSession(val gameSession: GameSession) : ServerActions()
    }
}
