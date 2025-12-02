package pl.msiwak.connection

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import pl.msiwak.connection.model.ClientActions
import pl.msiwak.connection.model.ServerActions
import pl.msiwak.connection.model.WebSocketEvent
import kotlin.reflect.KClass

object Json {

    lateinit var json: Json

    fun generateJson(
        events: List<Pair<KClass<out WebSocketEvent>, KSerializer<out WebSocketEvent>>>
    ) {
        json = Json {
            serializersModule = prepareSerializersModule(events)
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    private fun prepareSerializersModule(
        events: List<Pair<KClass<out WebSocketEvent>, KSerializer<out WebSocketEvent>>>
    ) = SerializersModule {
        polymorphic(WebSocketEvent::class) {
            subclass(ClientActions.UserConnected::class, ClientActions.UserConnected.serializer())
            subclass(ClientActions.UserDisconnected::class, ClientActions.UserDisconnected.serializer())
            subclass(ClientActions.ServerDownDetected::class, ClientActions.ServerDownDetected.serializer())
            subclass(ServerActions.ServerStarted::class, ServerActions.ServerStarted.serializer())
            events.forEach { (type, serializer) ->
                subclass(type as KClass<WebSocketEvent>, serializer as KSerializer<WebSocketEvent>)
            }
        }
    }
}