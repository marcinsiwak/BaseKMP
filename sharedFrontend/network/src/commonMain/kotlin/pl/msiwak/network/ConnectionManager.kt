package pl.msiwak.network

import kotlinx.coroutines.flow.Flow

interface ConnectionManager {

    fun getLocalIpAddress(): String?

    suspend fun findGame(port: Int): String?

    fun startUdpListener(port: Int = 60000): Flow<String>
    suspend fun broadcastMessage(msg: String, port: Int = 60000)
}