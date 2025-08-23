package pl.msiwak.network

expect class ConnectionManager() {

    fun getLocalIpAddress(): String?
}