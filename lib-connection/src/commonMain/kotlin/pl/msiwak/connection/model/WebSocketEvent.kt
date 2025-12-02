package pl.msiwak.connection.model

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
abstract class WebSocketEvent
