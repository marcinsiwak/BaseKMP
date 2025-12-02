package pl.msiwak.network

import kotlinx.coroutines.flow.Flow
import pl.msiwak.common.model.WifiState

interface ConnectionManager {

    fun observeWifiState(): Flow<WifiState>

    fun getLocalIpAddress(): String?

    suspend fun findGame(port: Int): String?

    fun startUdpListener(port: Int = 60000): Flow<String>
    suspend fun broadcastMessage(msg: String, port: Int = 60000)
}
