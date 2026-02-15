// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "TelegraphObjCWrapper",
    platforms: [
        .iOS(.v14),
        .macOS(.v11),
        .tvOS(.v14),
        .watchOS(.v7)
    ],
    products: [
        .library(
            name: "TelegraphObjCWrapper",
            targets: ["TelegraphObjCWrapper"]
        )
    ],
    dependencies: [
        .package(url: "https://github.com/Building42/Telegraph.git", from: "0.28.0")
    ],
    targets: [
        .target(
            name: "TelegraphObjCWrapper",
            dependencies: ["Telegraph"],
            path: "Sources/TelegraphObjCWrapper"
        )
    ]
)
