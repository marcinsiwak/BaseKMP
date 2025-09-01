package pl.msiwak.network

class ServerManager(
    private val ktorServer: KtorServer
) {
    fun startServer(host: String, port: Int) {
        ktorServer.startServer(host, port)
    }

    fun stopServer() {
       ktorServer.stopServer()
    }

}