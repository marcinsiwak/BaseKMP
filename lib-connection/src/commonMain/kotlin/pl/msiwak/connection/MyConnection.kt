package pl.msiwak.connection

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.KSerializer
import pl.msiwak.connection.model.WebSocketEvent
import kotlin.reflect.KClass

interface MyConnection {

    val isWifiConnected: StateFlow<Boolean>

    val isLoading: StateFlow<Boolean>
    val serverMessages: Flow<WebSocketEvent>
    val clientMessages: SharedFlow<WebSocketEvent>
    suspend fun send(id: String, webSocketEvent: WebSocketEvent)
    suspend fun sendToAll(webSocketEvent: WebSocketEvent)
    suspend fun sendFromClient(webSocketEvent: WebSocketEvent)

    fun getDeviceId(): String

    suspend fun connect()
    suspend fun disconnectUsers()

    fun isServerRunning(): Boolean

    fun setCustomEvents(events: List<Pair<KClass<out WebSocketEvent>, KSerializer<out WebSocketEvent>>>)
}