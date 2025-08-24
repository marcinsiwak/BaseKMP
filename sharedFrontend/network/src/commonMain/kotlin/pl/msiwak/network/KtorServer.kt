package pl.msiwak.network

interface KtorServer {
    fun startServer(host: String, port: Int)
    fun stopServer()
}