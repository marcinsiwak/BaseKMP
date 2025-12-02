package pl.msiwak.connection.model

import kotlinx.serialization.Serializable

@Serializable
sealed class ServerActions : WebSocketEvent() {
    @Serializable
    data object ServerStarted : ServerActions()
}