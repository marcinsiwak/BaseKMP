//
//  Pinger.swift
//  iosApp
//
//  Created by Marcin Siwak on 26/08/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import Network
import ComposeApp
import Combine
import FirebaseAnalytics
import FirebaseCrashlytics

class ConnectionManagerImpl: ConnectionManager {

    private let subject = PassthroughSubject<String, Never>()
    private let wifiStateSubject = PassthroughSubject<WifiState, Never>()
    private var listener: NWListener?
    private let monitor = NWPathMonitor(requiredInterfaceType: .wifi)
    private let queue = DispatchQueue.global(qos: .background)
    private var networkIp: String?

    func observeWifiState() -> any Kotlinx_coroutines_coreFlow {
        monitor.pathUpdateHandler = { [weak self] path in
            guard let self = self else {
                return
            }
            let state = (path.status == .satisfied && path.usesInterfaceType(.wifi)) ? WifiState.connected : WifiState.disconnected
            print("OUTPUT: \(state)")
            DispatchQueue.main.async {
                self.wifiStateSubject.send(state)
            }
        }
        monitor.start(queue: queue)
        return wifiStateSubject.asFlow()
    }

    func getBroadcastAddress() -> String? {
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

    func broadcastMessage(msg: String, port: Int32) async throws {
        if(networkIp == nil) {
            networkIp = getLocalIpAddress()
        }
        let hostIp = networkIp?.split(separator: ".").dropLast().joined(separator: ".") ?? ""
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

    func htons(_ value: UInt16) -> UInt16 {
        return (value << 8) | (value >> 8)
    }

    func startUdpListener(port: Int32) -> any Kotlinx_coroutines_coreFlow {
        let params = NWParameters.udp

        guard let portValue = NWEndpoint.Port(rawValue: UInt16(port)) else {
            print("❌ Invalid port: \(port)")
            Analytics.logEvent("test_connection", parameters: ["UDP_LISTEN": "invalid port"])

            let error = NSError(domain: "app.error", code: 0, userInfo: [NSLocalizedDescriptionKey: "UDP_LISTEN INVALID PORT"])

            return subject.asFlow()
        }

        do {
            let listener = try NWListener(using: params, on: portValue)
            self.listener = listener

            listener.newConnectionHandler = { connection in
                connection.start(queue: .global())

                connection.receiveMessage { data, _, _, error in
                    defer {
                        connection.cancel()
                    }

                    if let error = error {
                        print("UDP receive error: \(error)")
                        return
                    }

                    if let data, let message = String(data: data, encoding: .utf8) {
                        self.subject.send(message)
                    }
                }
            }

            listener.start(queue: .global())
            print("👂 Listening for UDP messages on port \(port)")
            Analytics.logEvent("test_connection", parameters: ["UDP_LISTEN": "listener success"])

        } catch {
            Analytics.logEvent("test_connection", parameters: ["UDP_LISTEN": "listener failed"])

            let error = NSError(domain: "app.error", code: 0, userInfo: [NSLocalizedDescriptionKey: "UDP_LISTEN LISTENER FAILED"])

            print("❌ Failed to start UDP listener on port \(port): \(error)")
        }
        return subject.asFlow()
    }

    func findGame(port: Int32, completionHandler: @escaping (String?, (any Error)?) -> Void) {
        guard let ownIp = getLocalIpAddress(), !ownIp.isEmpty else {
            completionHandler(nil, nil)
            return
        }

        let subnet = ownIp.split(separator: ".").dropLast().joined(separator: ".")

        var didReturn = false // Track if we've already returned

        for i in 1...254 {
            let host = subnet + ".\(i)"
            let nwEndpoint = NWEndpoint.Host(host)
            guard let nwPort = NWEndpoint.Port(rawValue: UInt16(port)) else {
                continue
            }

            let connection = NWConnection(host: nwEndpoint, port: nwPort, using: .tcp)

            connection.stateUpdateHandler = { state in
                guard !didReturn else {
                    return
                } // Skip if already found
                switch state {
                case .ready:
                    print("✅ First responding host: \(host):\(port)")
                    didReturn = true
                    connection.cancel()
                    completionHandler(host, nil)
                case .failed(_):
                    connection.cancel()
                default:
                    break
                }
            }

            connection.start(queue: .global())

            // Timeout
            DispatchQueue.global().asyncAfter(deadline: .now() + 1) {
                if !didReturn && connection.state != .ready {
                    connection.cancel()
                }
            }
        }

        // Safety fallback: return nil if no host responds
        DispatchQueue.global().asyncAfter(deadline: .now() + 1) {
            if !didReturn {
                didReturn = true
                completionHandler(nil, nil)
            }
        }
    }


    func getLocalIpAddress() -> String? {
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

        networkIp = address
        return address
    }
}

