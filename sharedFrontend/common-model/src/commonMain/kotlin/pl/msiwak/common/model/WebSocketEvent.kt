package pl.msiwak.common.model

import kotlinx.serialization.Serializable

@Serializable
sealed class WebSocketEvent() {

    @Serializable
    sealed class ClientActions : WebSocketEvent() {

        // connection events
        @Serializable
        data class PlayerConnected(val player: Player) : ClientActions()

        @Serializable
        data class PlayerClientDisconnected(val id: String) : ClientActions()

        @Serializable
        data object ServerDownDetected : ClientActions()

        // game events
        @Serializable
        data class SetPlayerReady(val id: String) : ClientActions()

        @Serializable
        data class AddCard(val id: String, val cardText: String) : ClientActions()

        @Serializable
        data object ContinueGame : ClientActions()
    }

    @Serializable
    sealed class ServerActions : WebSocketEvent() {
        @Serializable
        data class UpdateGameSession(val gameSession: GameSession) : ServerActions()
    }
}
