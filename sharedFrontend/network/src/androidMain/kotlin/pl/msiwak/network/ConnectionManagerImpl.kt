package pl.msiwak.network

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.RequiresPermission
import pl.msiwak.common.AppContext
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class ConnectionManagerImpl : ConnectionManager {

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

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun findGame(port: Int): String? {
        val ownIp = getLocalIpAddress() ?: throw Exception("Connect to network")
        val subnet = ownIp.substringBeforeLast(".")

        for (i in 1..10) {
            val host = "192.168.0.224"
            runCatching {
                Log.d("NetworkScan", "Scanning host $host")
                if (InetAddress.getByName(host).isReachable(1000)) {
                    val socket = Socket()
                    Log.d("NetworkScan", "Scanning port $port")
                    socket.connect(InetSocketAddress(host, port), 200)
                    socket.close()
                    Log.d("NetworkScan", "Device with open port $port found: $host")
                    return host
                }
            }
        }
        return null
    }
}
