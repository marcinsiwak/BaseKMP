// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "_lib_connection",
  platforms: [
    .iOS("15.0"),
  ],
  products: [
      .library(
          name: "_lib_connection",
          type: .none,
          targets: ["_lib_connection"]
      ),
  ],
  dependencies: [
    .package(
      path: "/Users/msiwak/AndroidStudioProjects/CardsTheGame/lib-connection/TelegraphWrapper",
    ),
    .package(
      path: "/Users/msiwak/AndroidStudioProjects/CardsTheGame/lib-connection/NetworkWrapper",
    ),
  ],
  targets: [
    .target(
      name: "_lib_connection",
      dependencies: [
        .product(
          name: "TelegraphObjCWrapper",
          package: "TelegraphWrapper",
          condition: .when(platforms: [.iOS]),
        ),
        .product(
          name: "NetworkWrapper",
          package: "NetworkWrapper",
          condition: .when(platforms: [.iOS]),
        ),
      ]
    ),
  ]
)
