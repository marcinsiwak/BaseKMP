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
            Analytics.logEvent("test_connection", parameters: ["wifiState": state.name])
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

        FirebaseCrashlytics.Crashlytics.crashlytics().record(error: error)
    
        return nil
    }

    func broadcastMessage(msg: String, port: Int32) async throws {
        let data = msg.data(using: .utf8)!
        if(networkIp == nil) {
            networkIp = getLocalIpAddress()
        }
        let hostIp = networkIp?.split(separator: ".").dropLast().joined(separator: ".") ?? ""
        let host = NWEndpoint.Host(hostIp + ".255")
        let remotePort = NWEndpoint.Port(integerLiteral: UInt16(port))

        let parameters = NWParameters.udp
        let connection = NWConnection(
            host: host,
            port: remotePort,
            using: parameters
        )


        connection.start(queue: .global())

        connection.stateUpdateHandler = { newState in
            switch newState {
            case .ready:
                connection.send(content: data, completion: .contentProcessed { error in
                    if let error = error {
                        print("❌ Error sending UDP message: \(error)")
                        Analytics.logEvent("test_connection", parameters: ["UDP_SEND": "error: \(error)"])

                        let error = NSError(domain: "app.error", code: 0, userInfo: [NSLocalizedDescriptionKey: "UDP_SEND error"])

                        FirebaseCrashlytics.Crashlytics.crashlytics().record(error: error)

                    } else {
                        // print("✅ Message sent: \"\(msg)\"")
                    }
                    connection.cancel()
                })

            case .failed(let error):
                print("❌ Connection failed with error: \(error)")
                Analytics.logEvent("test_connection", parameters: ["UDP_SEND": "connection failed: \(error)"])
                
                
                let error = NSError(domain: "app.error", code: 0, userInfo: [NSLocalizedDescriptionKey: "UDP_SEND failed"])

                FirebaseCrashlytics.Crashlytics.crashlytics().record(error: error)

                connection.cancel()

            case .cancelled:
               // Analytics.logEvent("test_connection", parameters: ["UDP_SEND": "connection cancelled"])

                ""
                // print("Connection closed.")

            default:
                break
            }
        }
    }

    func startUdpListener(port: Int32) -> any Kotlinx_coroutines_coreFlow {
        let params = NWParameters.udp
        params.allowLocalEndpointReuse = true

        guard let portValue = NWEndpoint.Port(rawValue: UInt16(port)) else {
            print("❌ Invalid port: \(port)")
            Analytics.logEvent("test_connection", parameters: ["UDP_LISTEN": "invalid port"])
            
            
            let error = NSError(domain: "app.error", code: 0, userInfo: [NSLocalizedDescriptionKey: "UDP_LISTEN INVALID PORT"])

            FirebaseCrashlytics.Crashlytics.crashlytics().record(error: error)

            return subject.asFlow()
        }

        do {
            let listener = try NWListener(using: params, on: portValue)
            self.listener = listener

            listener.newConnectionHandler = { connection in
                connection.start(queue: .global())

                connection.receiveMessage { data, _, _, error in
                    if let error = error {
                        print("UDP receive error: \(error)")
                        Analytics.logEvent("test_connection", parameters: ["UDP_LISTEN": "receive error"])
                        
                        let error = NSError(domain: "app.error", code: 0, userInfo: [NSLocalizedDescriptionKey: "UDP_LISTEN RECEIVE ERROR"])

                        FirebaseCrashlytics.Crashlytics.crashlytics().record(error: error)

                        return
                    }
                    if let data = data, let message = String(data: data, encoding: .utf8) {
                        // print("📨 Received: \(message)")
                        // Analytics.logEvent("test_connection", parameters: ["UDP_LISTEN": "received success"])

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

            FirebaseCrashlytics.Crashlytics.crashlytics().record(error: error)

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
        Analytics.logEvent("test_connection", parameters: ["LOCAL_ADDRESS": address])
        
        
        let error = NSError(domain: "app.error", code: 0, userInfo: [NSLocalizedDescriptionKey: "LOCAL IP \(String(describing: address))"])

        FirebaseCrashlytics.Crashlytics.crashlytics().record(error: error)

        networkIp = address
        return address
    }
}

