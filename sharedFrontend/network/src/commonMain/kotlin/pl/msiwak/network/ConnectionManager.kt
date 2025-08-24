package pl.msiwak.network

expect class ConnectionManager() {

    fun getLocalIpAddress(): String?

    suspend fun findGame(port: Int): String
}