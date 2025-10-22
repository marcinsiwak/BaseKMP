package pl.msiwak.network

import kotlinx.coroutines.flow.Flow

interface KtorServer {

    val messages: Flow<String>
    fun startServer(host: String, port: Int)
    suspend fun stopServer()

    suspend fun sendMessage(userId: String, message: String)
    suspend fun sendMessageToAll(message: String)

    suspend fun closeSocker(userId: String)

    suspend fun closeAllSockets()
}