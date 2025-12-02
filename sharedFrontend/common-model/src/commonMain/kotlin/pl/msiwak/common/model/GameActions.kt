package pl.msiwak.common.model

import kotlinx.serialization.Serializable
import pl.msiwak.connection.model.WebSocketEvent

@Serializable
sealed class GameActions : WebSocketEvent() {

    // game events
    @Serializable
    data class SetPlayerReady(val id: String) : GameActions()

    @Serializable
    data class JoinTeam(val id: String, val teamName: String) : GameActions()

    @Serializable
    data class AddCard(val id: String, val cardText: String) : GameActions()

    @Serializable
    data class SetCorrectAnswer(val cardText: String) : GameActions()

    @Serializable
    data object ContinueGame : GameActions()

    @Serializable
    data class AddPlayerName(val id: String, val name: String) : GameActions()
}
