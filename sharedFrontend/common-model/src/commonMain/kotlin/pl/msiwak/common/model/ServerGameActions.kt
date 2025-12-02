package pl.msiwak.common.model

import kotlinx.serialization.Serializable
import pl.msiwak.connection.model.WebSocketEvent

@Serializable
sealed class ServerGameActions : WebSocketEvent() {
    @Serializable
    data class UpdateGameSession(val gameSession: GameSession) : ServerGameActions()
}