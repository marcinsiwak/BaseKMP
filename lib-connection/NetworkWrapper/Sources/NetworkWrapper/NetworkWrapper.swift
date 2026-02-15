import Foundation
import Network

@available(iOS 13.0, *)
@MainActor
@objc
public class NetworkWrapper: NSObject {

    private var listener: NWListener?
    private var connection: NWConnection?

    // MARK: - Callbacks exposed to Kotlin

    @objc public var onMessage: ((String) -> Void)?
    @objc public var onStateChanged: ((String) -> Void)?

    // MARK: - Start Server

    @objc
    public func startUDPListener(port: UInt16) {
        do {
            let nwPort = NWEndpoint.Port(rawValue: port)!

            listener = try NWListener(using: .udp, on: nwPort)

            listener?.stateUpdateHandler = { [weak self] state in
                Task { @MainActor in
                    self?.onStateChanged?("\(state)")
                }
            }

            listener?.newConnectionHandler = { [weak self] newConnection in
                Task { @MainActor in
                    guard let self = self else {
                        return
                    }
                    self.connection = newConnection
                    self.setupReceive(on: newConnection)
                    newConnection.start(queue: .main)
                }
            }

            listener?.start(queue: .main)

        } catch {
            onStateChanged?("failed: \(error)")
        }
    }

    // MARK: - Receive Loop

    private func setupReceive(on connection: NWConnection) {
        connection.receive(
            minimumIncompleteLength: 1,
            maximumLength: 65536
        ) { [weak self] data, _, isComplete, error in
            Task { @MainActor in
                if let data, let message = String(data: data, encoding: .utf8) {
                    self?.onMessage?(message)
                }
                if error == nil && !isComplete {
                    if let connection = self?.connection {
                        self?.setupReceive(on: connection)
                    } else {
                        // If connection was deallocated, continue receiving on the passed-in connection
                        self?.setupReceive(on: connection)
                    }
                }
            }
        }
    }

    // MARK: - Send

    @objc
    public func send(data: Data) {
        if let connection = self.connection {
            connection.send(
                content: data,
                completion: .contentProcessed { _ in
                }
            )
        }
    }

    // MARK: - Stop

    @objc
    public func stop() {
        connection?.cancel()
        listener?.cancel()
        connection = nil
        listener = nil
    }

    @objc
    public func sendBroadcast(
        msg: String,
        networkIp: String,
        port: Int32
    ) async throws {
        let hostIp = networkIp.split(separator: ".").dropLast().joined(separator: ".")
        let broadcastIp = getBroadcastAddress() ?? hostIp + ".255"

        guard port >= 0 && port <= 65535 else {
            throw NSError(domain: "Invalid port", code: 0)
        }
        let portUInt16 = UInt16(port)  // ✅ convert Int32 to UInt16

        let sock = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)
        guard sock >= 0 else { throw NSError(domain: "socket error", code: 0) }

        // Enable broadcast
        var broadcastEnable: Int32 = 1
        setsockopt(sock, SOL_SOCKET, SO_BROADCAST, &broadcastEnable, socklen_t(MemoryLayout.size(ofValue: broadcastEnable)))

        // Setup broadcast address
        var addr = sockaddr_in()
        addr.sin_family = sa_family_t(AF_INET)
        addr.sin_port = htons(portUInt16) // ✅ use UInt16
        addr.sin_addr.s_addr = inet_addr(broadcastIp) // your subnet broadcast

        let data = [UInt8](msg.utf8)
        let sent = withUnsafePointer(to: &addr) { ptr -> Int in
            let addrPtr = UnsafeRawPointer(ptr).assumingMemoryBound(to: sockaddr.self)
            return sendto(sock, data, data.count, 0, addrPtr, socklen_t(MemoryLayout<sockaddr_in>.size))
        }

        if sent < 0 {
            perror("sendto")
        }

        close(sock)
    }

    private func getBroadcastAddress() -> String? {
        var ifaddr: UnsafeMutablePointer<ifaddrs>?
        guard getifaddrs(&ifaddr) == 0 else {
            return nil
        }
        defer {
            freeifaddrs(ifaddr)
        }

        var ptr = ifaddr
        while ptr != nil {
            defer {
                ptr = ptr?.pointee.ifa_next
            }
            guard let interface = ptr?.pointee else {
                continue
            }

            let addrFamily = interface.ifa_addr.pointee.sa_family
            if addrFamily == UInt8(AF_INET) {
                let name = String(cString: interface.ifa_name)
                if name == "en0" { // Wi-Fi on iOS/macOS
                    var addr = interface.ifa_addr.withMemoryRebound(to: sockaddr_in.self, capacity: 1) {
                        $0.pointee
                    }
                    var netmask = interface.ifa_netmask.withMemoryRebound(to: sockaddr_in.self, capacity: 1) {
                        $0.pointee
                    }

                    let broadcast = (addr.sin_addr.s_addr & netmask.sin_addr.s_addr) | ~netmask.sin_addr.s_addr
                    var bcast = in_addr(s_addr: broadcast)

                    var buffer = [CChar](repeating: 0, count: Int(INET_ADDRSTRLEN))
                    inet_ntop(AF_INET, &bcast, &buffer, socklen_t(INET_ADDRSTRLEN))
                    return String(cString: buffer)
                }
            }
        }

        let error = NSError(domain: "app.error", code: 0, userInfo: [NSLocalizedDescriptionKey: "Address not found"])

        return nil
    }

    private func htons(_ value: UInt16) -> UInt16 {
        return (value << 8) | (value >> 8)
    }

    @objc
    public func getLocalIpAddress() -> String? {
        var address: String?
        var ifaddr: UnsafeMutablePointer<ifaddrs>?

        if getifaddrs(&ifaddr) == 0 {
            var ptr = ifaddr
            while ptr != nil {
                defer {
                    ptr = ptr?.pointee.ifa_next
                }

                guard let interface = ptr?.pointee else {
                    continue
                }
                let addrFamily = interface.ifa_addr.pointee.sa_family

                // Only IPv4
                if addrFamily == UInt8(AF_INET) {
                    let name = String(cString: interface.ifa_name)

                    // Match Wi-Fi interfaces (and exclude loopback/bridges)
                    if (name == "en0" || name == "wifi0" || name == "wlan0"),
                       !name.contains("bridge") {

                        var hostname = [CChar](repeating: 0, count: Int(NI_MAXHOST))
                        if getnameinfo(
                            interface.ifa_addr,
                            socklen_t(interface.ifa_addr.pointee.sa_len),
                            &hostname,
                            socklen_t(hostname.count),
                            nil,
                            0,
                            NI_NUMERICHOST
                        ) == 0 {
                            let ip = String(cString: hostname)

                            // Ensure it's private LAN
                            if ip.hasPrefix("192.168.") ||
                                   ip.hasPrefix("10.") ||
                                   ip.range(of: #"^172\.(1[6-9]|2[0-9]|3[01])\."#, options: .regularExpression) != nil {
                                address = ip
                                break
                            }
                        }
                    }
                }
            }
            freeifaddrs(ifaddr)
        }
        let error = NSError(domain: "app.error", code: 0, userInfo: [NSLocalizedDescriptionKey: "LOCAL IP \(String(describing: address))"])

        return address
    }
}
