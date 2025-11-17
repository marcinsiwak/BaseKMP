package pl.msiwak.network

import kotlinx.coroutines.flow.Flow

interface ConnectionManager {

    suspend fun checkWifiIsOn(): WifiState

    fun getLocalIpAddress(): String?

    suspend fun findGame(port: Int): String?

    fun startUdpListener(port: Int = 60000): Flow<String>
    suspend fun broadcastMessage(msg: String, port: Int = 60000)
}

data class WifiState(val isRunning: Boolean)