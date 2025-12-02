package pl.msiwak.network

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.NetworkRequest.Builder
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import pl.msiwak.common.AppContext
import pl.msiwak.common.model.WifiState
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException


class ConnectionManagerImpl : ConnectionManager {

    private val request: NetworkRequest
        get() = Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun observeWifiState(): Flow<WifiState> {
        val context = AppContext.get()
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val isWifi = with(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)) {
            if (this == null) return@with false
            hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }

        return callbackFlow {
            if (isWifi) {
                trySend(WifiState.CONNECTED)
            } else {
                trySend(WifiState.DISCONNECTED)
            }

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    this@callbackFlow.trySend(WifiState.CONNECTED)

                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    this@callbackFlow.trySend(WifiState.DISCONNECTED)
                }
            }
            connectivityManager.registerNetworkCallback(
                request,
                callback
            )

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun getLocalIpAddress(): String? {
        val context = AppContext.get()
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return null
        val linkProperties = connectivityManager.getLinkProperties(network) ?: return null

        for (linkAddress in linkProperties.linkAddresses) {
            val address = linkAddress.address
            if (address is Inet4Address && !address.isLoopbackAddress) {
                return address.hostAddress
            }
        }
        return null
    }

    override fun startUdpListener(port: Int): Flow<String> = callbackFlow {
        val socket = DatagramSocket(port)
        socket.soTimeout = 1000
        println("[UDP] Started")

        val buffer = ByteArray(1024)
        val job = launch(Dispatchers.IO) {
            try {
                while (isActive) {
                    try {
                        val packet = DatagramPacket(buffer, buffer.size)
                        socket.receive(packet)
                        val sender = packet.address.hostAddress
                        val message = String(packet.data, 0, packet.length)
                        println("[UDP] Received from $sender: \"$message\"")
                        if (!sender.isNullOrBlank()) {
                            trySend(message)
                        }
                    } catch (e: SocketTimeoutException) {
                        println("[UDP] error Received from: $e")
                    }
                }
            } finally {
                socket.close()
            }
        }

        awaitClose {
            job.cancel()
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun broadcastMessage(msg: String, port: Int): Unit = withContext(Dispatchers.IO) {
        runCatching {
            val socket = DatagramSocket()
            socket.broadcast = true
            val data = msg.toByteArray()
            val packet = DatagramPacket(
                data, data.size,
                InetAddress.getByName("255.255.255.255"), port
            )
            socket.send(packet)
            socket.close()
        }.onFailure {
            println("ERROR: ${it.message}")
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun findGame(port: Int): String? {
        val ownIp = getLocalIpAddress() ?: throw Exception("Connect to network")
        val subnet = ownIp.substringBeforeLast(".")

        Log.d("NetworkScan", "Starting network scan for port $port on subnet $subnet.x")

        return coroutineScope {
            val scanTasks = (1..254).map { i ->
                async {
                    val host = "$subnet.$i"
                    scanHost(host, port)
                }
            }
            for (task in scanTasks) {
                val result = task.await()
                if (result != null) {
                    Log.d("NetworkScan", "Game found at $result, cancelling remaining scans")
                    scanTasks.forEach { if (it != task) it.cancel() }
                    return@coroutineScope result
                }
            }
            Log.d("NetworkScan", "No game server found on subnet $subnet.x")
            null
        }
    }

    private suspend fun scanHost(host: String, port: Int): String? {
        return withTimeoutOrNull(1000) { // 1 second timeout per host
            runCatching {
                Log.d("NetworkScan", "Scanning $host")

                // First check if host is reachable
                if (!InetAddress.getByName(host).isReachable(100)) {
                    return@runCatching null
                }

                Log.d("NetworkScan", "$host is reachable, checking port $port")

                // Then check if port is open
                Socket().use { socket ->
                    socket.connect(InetSocketAddress(host, port), 500)
                    Log.d("NetworkScan", "Game server found at $host:$port")
                    host
                }
            }.onFailure { e ->
                when (e) {
                    is SocketTimeoutException -> Log.v("NetworkScan", "$host:$port - connection timeout")
                    is IOException -> Log.v("NetworkScan", "$host:$port - connection failed: ${e.message}")
                    else -> Log.w("NetworkScan", "$host:$port - unexpected error: ${e.message}")
                }
            }.getOrNull()
        }
    }
}
