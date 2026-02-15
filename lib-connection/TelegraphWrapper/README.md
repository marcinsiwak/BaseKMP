# TelegraphObjCWrapper

An Objective‑C–friendly Swift wrapper around the Telegraph WebSocket/HTTP server library. Use it from Swift, Objective‑C, and Kotlin Multiplatform (iOS) via Swift/ObjC interop.

> Note: The wrapper compiles without Telegraph by default. To enable Telegraph, add it as a dependency in `Package.swift` and uncomment `import Telegraph` in the source.

## Requirements
- iOS 14+, macOS 11+, tvOS 14+, watchOS 7+
- Swift tools 5.9+

## Installation

### Swift Package Manager
- Xcode: File → Add Packages… → Enter the repo URL for this package
- Select the `TelegraphObjCWrapper` product and add it to your target.

## Usage

### Swift
```swift
import TelegraphObjCWrapper

final class MyDelegate: NSObject, TelegraphObjCWrapperDelegate {
    func telegraphServerDidStart(host: String, port: Int) {
        print("Server started on \(host):\(port)")
    }
}

let server = TelegraphObjCWrapper()
server.delegate = MyDelegate()
server.start(host: "0.0.0.0", port: 8080)
