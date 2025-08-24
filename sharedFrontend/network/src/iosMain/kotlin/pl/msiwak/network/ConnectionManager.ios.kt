package pl.msiwak.network

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import platform.darwin.freeifaddrs
import platform.darwin.getifaddrs
import platform.darwin.ifaddrs
import platform.posix.AF_INET
import platform.posix.NI_MAXHOST
import platform.posix.NI_NUMERICHOST
import platform.posix.getnameinfo
import platform.posix.socklen_t

actual class ConnectionManager {

    @OptIn(ExperimentalForeignApi::class)
    actual fun getLocalIpAddress(): String? {
        memScoped {
            val ifaddrPtr = alloc<CPointerVar<ifaddrs>>()
            if (getifaddrs(ifaddrPtr.ptr) != 0) return null
            val firstAddr = ifaddrPtr.value ?: return null

            runCatching {
                var ptr: CPointer<ifaddrs>? = firstAddr
                while (ptr != null) {
                    val interf = ptr.pointed
                    val addr = interf.ifa_addr ?: continue
                    if (addr.pointed.sa_family.toInt() == AF_INET) {
                        val name = interf.ifa_name?.toKString() ?: ""
                        if (name == "en0" || name == "pdp_ip0") {
                            val hostname = ByteArray(NI_MAXHOST)
                            if (getnameinfo(
                                    addr,
                                    addr.pointed.sa_len.toInt().convert<socklen_t>(),  // Use convert() instead
                                    hostname.refTo(0),
                                    hostname.size.convert(),
                                    null,
                                    0u,
                                    NI_NUMERICHOST
                                ) == 0
                            ) {
                                return hostname.toKString()
                            }
                        }
                    }
                    ptr = interf.ifa_next
                }
            }.also {
                freeifaddrs(firstAddr)
            }.getOrThrow()
        }
        return null
    }
}


