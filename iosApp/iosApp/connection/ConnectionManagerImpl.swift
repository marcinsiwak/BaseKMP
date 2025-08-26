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
import sharedFrontend

class ConnectionManagerImpl: ConnectionManager {
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
                guard let nwPort = NWEndpoint.Port(rawValue: UInt16(port)) else { continue }

                let connection = NWConnection(host: nwEndpoint, port: nwPort, using: .tcp)

                connection.stateUpdateHandler = { state in
                    guard !didReturn else { return } // Skip if already found
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
                defer { ptr = ptr?.pointee.ifa_next }
                
                guard let interface = ptr?.pointee else { continue }
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
        return address
    }
    
}
