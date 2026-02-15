import Foundation
import ComposeApp
import SwiftUI
import FirebaseCore
import FirebaseCrashlytics

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        IOSAppKt.doInitKoin()
        MyConnectionDI().doInitKoin()
        FirebaseApp.configure()

        return true
    }
}
