package pl.msiwak.network

interface ConnectionManager {

    fun getLocalIpAddress(): String?

    suspend fun findGame(port: Int): String?
}