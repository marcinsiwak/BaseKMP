@file:OptIn(ExperimentalForeignApi::class)

package pl.msiwak.connection

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import platform.Foundation.NSError
import platform.darwin.NSObject
import swiftPMImport.CardsTheGame.lib.connection.TGServer
import swiftPMImport.CardsTheGame.lib.connection.TGServerWebSocketDelegateProtocol
import swiftPMImport.CardsTheGame.lib.connection.TGWebSocket

class KtorServerImpl() : KtorServer {

    private val _messages = MutableSharedFlow<String>()
    override val messages: Flow<String> = _messages.asSharedFlow()

    private var activeSessions = mutableMapOf<String, TGWebSocket>()

    private var server: TGServer? = null

    private val serverMutex = Mutex()
    private val mutex = Mutex()

    private val scope = CoroutineScope(Dispatchers.Main)

    val delegate: TGServerWebSocketDelegateProtocol = object : NSObject(), TGServerWebSocketDelegateProtocol {
        @ObjCSignatureOverride
        override fun telegraphServer(
            server: TGServer,
            webSocket: TGWebSocket,
            didReceiveText: String
        ) {
            println("OUTPUT: Received message: $didReceiveText")
            scope.launch {
                _messages.emit(didReceiveText)
            }
        }

        override fun telegraphServer(
            server: TGServer,
            webSocketDidDisconnect: TGWebSocket,
            error: NSError?
        ) {
            println("OUTPUT: Connection disconnected with error: ${error?.localizedDescription}")

            activeSessions.entries.firstOrNull { it.value == webSocketDidDisconnect }?.key?.let {
                activeSessions.remove(it)
                scope.launch {
                    _messages.emit("Client disconnected: $it")
                }
            }
        }

        @ObjCSignatureOverride
        override fun telegraphServer(
            server: TGServer,
            webSocketDidConnect: TGWebSocket,
            path: String,
            id: String
        ) {
            println("OUTPUT: Connection established with id: $id")
            activeSessions[id] = webSocketDidConnect
        }
    }

    override suspend fun startServer(host: String, port: Int) {
        serverMutex.withLock {
            if (server != null) {
                println("OUTPUT: Server already running")
                return
            }
            server = TGServer()
            server?.setConcurrencyWithConcurencyNumber(5) // change name in wrapper
            println("OUTPUT: Server starting on $host:$port")
            _messages.emit("Server started")
            server?.setWebSocketDelegate(
                webSocketDelegate = delegate
            )

            server?.startOnPort(
                port = port.toLong(),
                error = null
            )
        }
    }

    override suspend fun stopServer() {
        server?.stop()
    }

    override suspend fun sendMessage(userId: String, message: String) {
        mutex.withLock {
            activeSessions[userId]?.sendText(message)
        }
    }

    override suspend fun sendMessageToAll(message: String) {
        mutex.withLock {
            activeSessions.values.forEach { it.sendText(message) }
        }
    }

    override suspend fun closeSocket(userId: String) {
        val session = mutex.withLock {
            activeSessions.remove(userId)
        }
        session?.close()
        activeSessions.remove(userId)
    }

    override suspend fun closeAllSockets() {
        mutex.withLock {
            val list = activeSessions.values.toList()
            activeSessions.clear()
            list
        }.forEach { it.close() }
    }

    override fun isRunning(): Boolean {
        return server != null && server?.isRunning() == true
    }
}
