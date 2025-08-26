package pl.msiwak.network

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import platform.darwin.freeifaddrs
import platform.darwin.getifaddrs
import platform.darwin.ifaddrs
import platform.posix.AF_INET
import platform.posix.NI_MAXHOST
import platform.posix.NI_NUMERICHOST
import platform.posix.SOCK_STREAM
import platform.posix.close
import platform.posix.connect
import platform.posix.gethostbyname
import platform.posix.getnameinfo
import platform.posix.in_addr
import platform.posix.memcpy
import platform.posix.memset
import platform.posix.sockaddr_in
import platform.posix.socket
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
                        // Check for WiFi interfaces and exclude loopback/cellular
                        if ((name.startsWith("en") || name == "wifi0" || name == "wlan0") &&
                            name != "en1" && // exclude cellular on some devices
                            !name.contains("bridge")
                        ) {
                            val hostname = ByteArray(NI_MAXHOST)
                            if (getnameinfo(
                                    addr,
                                    addr.pointed.sa_len.toInt().convert<socklen_t>(),
                                    hostname.refTo(0),
                                    hostname.size.convert(),
                                    null,
                                    0u,
                                    NI_NUMERICHOST
                                ) == 0
                            ) {
                                val ip = hostname.toKString()
                                // Ensure it's a private WiFi network IP
                                if (ip.startsWith("192.168.") ||
                                    ip.startsWith("10.") ||
                                    ip.matches(Regex("172\\.(1[6-9]|2[0-9]|3[01])\\..*"))
                                ) {
                                    return ip
                                }
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

    actual suspend fun findGame(port: Int): String? {
        val ownIp = getLocalIpAddress() ?: throw Exception("Connect to network")
        val subnet = ownIp.substringBeforeLast(".")

        for (i in 1..254) {
            val host = "$subnet.$i"
            println("Scanning host: $host")
//            val open = runCatching {
//                val isOpen = isPortOpen(host, port, 1000)
//                println("Host $host open? $isOpen")
//                isOpen
//            }.getOrElse {
//                println("Error scanning $host: $it")
//                false
//            }
//            println("Host $host open? $open")
//            }.onFailure { exception ->
//                println("Error checking host $host: ${exception}")
//            }
        }
        return "aaaaa"
    }
}

@OptIn(ExperimentalForeignApi::class)
suspend fun isPortOpen(host: String, port: Int, timeoutMillis: Int): Boolean {
    return try {
        memScoped {
            println("Checking $host:$port")
            val sock = socket(AF_INET, SOCK_STREAM, 0)
            println("Socket: $sock")
            if (sock < 0) return false
            println("Socket opened")

            try {
                println("Connecting...")
                val serverAddr = alloc<sockaddr_in>().apply {
                    memset(this.ptr, 0, sizeOf<sockaddr_in>().convert())
                    sin_family = AF_INET.convert()
                    sin_port = htons(port.toUShort())

                    val hostEntry = gethostbyname(host)
                    println("Host entry: $hostEntry")
                    if (hostEntry == null || hostEntry.pointed.h_addr_list == null) return false

                    println("Host entry has ${hostEntry.pointed.h_addr_list!!.pointed.value} addresses")
                    val addrList: CPointer<CPointerVar<ByteVar>> = hostEntry.pointed.h_addr_list!!
                    val addrPtr: CPointer<ByteVar>? = addrList.pointed.value
                    if (addrPtr == null) return false
                    println("First address: ${addrPtr.pointed.value}")
                    memcpy(sin_addr.ptr, addrPtr, sizeOf<in_addr>().convert())
                }

                println("Connecting to ${serverAddr.sin_addr.s_addr}:${serverAddr.sin_port.toUShort()}")
                val result = connect(sock, serverAddr.ptr.reinterpret(), sizeOf<sockaddr_in>().convert())
                println("Connect result: $result")
                result == 0
            } finally {
                println("Closing socket")
                close(sock)
            }
        }
    } catch (e: Exception) {
        println("HERE: Exception in isPortOpen: $e")
        false
    }
}

private fun htons(value: UShort): UShort {
    val v = value.toInt()
    return (((v and 0xFF) shl 8) or ((v ushr 8) and 0xFF)).toUShort()
}