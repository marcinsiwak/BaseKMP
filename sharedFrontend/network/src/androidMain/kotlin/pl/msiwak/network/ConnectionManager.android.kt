package pl.msiwak.network

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission
import pl.msiwak.common.AppContext
import java.net.Inet4Address

actual class ConnectionManager {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    actual fun getLocalIpAddress(): String? {
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
}
