package pl.msiwak.connection

import kotlinx.coroutines.flow.Flow

interface KtorServer {

    val messages: Flow<String>
    suspend fun startServer(host: String, port: Int)
    suspend fun stopServer()

    suspend fun sendMessage(userId: String, message: String)
    suspend fun sendMessageToAll(message: String)

    suspend fun closeSocket(userId: String)

    suspend fun closeAllSockets()

    fun isRunning(): Boolean
}