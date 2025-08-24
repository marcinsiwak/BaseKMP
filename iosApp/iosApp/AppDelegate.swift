import Foundation
import ComposeApp
import sharedFrontend
import SwiftUI

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
            IOSAppKt.doInitKoin { koinApp in
                
                koinApp.provideNetworkSwiftLibDependencyProvider(
                    diProvider: DIProviderImpl.shared
                )
            }
            
            return true
        }
}
