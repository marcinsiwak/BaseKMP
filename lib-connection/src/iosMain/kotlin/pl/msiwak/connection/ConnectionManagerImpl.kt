package pl.msiwak.connection

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import pl.msiwak.connection.model.WifiState
import platform.Network.nw_interface_type_wifi
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create_with_type
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_uses_interface_type
import platform.darwin.DISPATCH_QUEUE_PRIORITY_BACKGROUND
import platform.darwin.dispatch_get_global_queue
import swiftPMImport.CardsTheGame.lib.connection.NetworkWrapper

@OptIn(ExperimentalForeignApi::class)
class ConnectionManagerImpl : ConnectionManager {

    private val udpListenerWrapper = NetworkWrapper()

    override fun observeWifiState(): Flow<WifiState> = callbackFlow {
        val monitor = nw_path_monitor_create_with_type(nw_interface_type_wifi)
        val queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND.toLong(), 0u)

        nw_path_monitor_set_update_handler(monitor) { path ->
            val satisfied = nw_path_get_status(path) == nw_path_status_satisfied
            val usesWifi = nw_path_uses_interface_type(path, nw_interface_type_wifi)
            val state = if (satisfied && usesWifi) WifiState.CONNECTED else WifiState.DISCONNECTED
            trySend(state)
        }

        nw_path_monitor_set_queue(monitor, queue)
        nw_path_monitor_start(monitor)

        awaitClose {
            nw_path_monitor_cancel(monitor)
        }
    }

    override fun getLocalIpAddress(): String? = udpListenerWrapper.getLocalIpAddress()

    override fun startUdpListener(port: Int): Flow<String> {
        udpListenerWrapper.startUDPListenerWithPort(port.convert())

        return callbackFlow {
            udpListenerWrapper.setOnMessage { message ->
                message?.let { trySend(message) }
            }
            awaitClose {
                close()
            }
        }
    }

    private var networkIp: String? = null

    override suspend fun broadcastMessage(msg: String, port: Int) {
        if (networkIp == null) {
            networkIp = getLocalIpAddress()
        }
        udpListenerWrapper.sendBroadcastWithMsg(msg, networkIp ?: return, port, {})
    }
}
