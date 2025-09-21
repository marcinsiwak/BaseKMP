package pl.msiwak.common.model

import kotlinx.serialization.Serializable

@Serializable
sealed class WebSocketEvent() {

    @Serializable
    data class UpdateGameSession(val gameSession: GameSession) : WebSocketEvent()

    @Serializable
    data class PlayerConnected(val player: Player) : WebSocketEvent()

    @Serializable
    data class DisplayCurrentUsers(val currentPlayers: List<Player>) : WebSocketEvent()

    @Serializable
    data class PlayerDisconnected(val currentPlayers: List<Player>) : WebSocketEvent()

    @Serializable
    data class PlayerClientDisconnected(val id: String) : WebSocketEvent()

    @Serializable
    data object Error : WebSocketEvent()

    @Serializable
    data class GameLobby(val id: String) : WebSocketEvent()

    @Serializable
    data object ServerDown : WebSocketEvent()

    @Serializable
    data class SetPlayerReady(val id: String) : WebSocketEvent()
}
