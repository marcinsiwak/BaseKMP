import Foundation
import ComposeApp
import SwiftUI

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
            IOSAppKt.doInitKoin()
            
            return true
        }
}
