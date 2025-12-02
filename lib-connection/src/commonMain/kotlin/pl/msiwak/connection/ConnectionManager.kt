package pl.msiwak.connection

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import pl.msiwak.connection.model.WifiState

interface ConnectionManager {

    fun observeWifiState(): Flow<WifiState>

    fun getLocalIpAddress(): String?

    suspend fun findGame(port: Int): String?

    fun startUdpListener(port: Int = 60000): Flow<String>
    suspend fun broadcastMessage(msg: String, port: Int = 60000)
}
