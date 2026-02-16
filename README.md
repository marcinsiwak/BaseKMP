# BaseKMP - Kotlin Multiplatform Template

This is a production-ready Kotlin Multiplatform project template targeting Android, iOS, Web (WasmJS), and Server (Ktor).

## 📚 Documentation

### For AI Agents & Developers

> **⚡ [QUICK START](QUICK_START.md)** - 30-second overview + essential templates  
> **📖 [Documentation Index](DOCUMENTATION_INDEX.md)** - Complete documentation hub with search and navigation

### Quick Start Guides
- **[🚀 Quick Start Card](QUICK_START.md)** - **START HERE** - 30-second overview with copy-paste templates
- **[🎯 AI Agent Onboarding](AI_AGENT_ONBOARDING.md)** - Complete onboarding guide for AI agents
- **[⚡ Quick Reference Guide](AI_QUICK_REFERENCE.md)** - One-page cheat sheet for common operations
- **[📐 Architecture Overview](ARCHITECTURE.md)** - Visual architecture diagrams and data flow
- **[🤔 Decision Tree](DECISION_TREE.md)** - Flowcharts for making architectural decisions
- **[📖 Complete Guidelines](AI_AGENT_GUIDELINES.md)** - Comprehensive development guide with best practices

### Quick Navigation
| Document | Best For |
|----------|----------|
| [Quick Start](QUICK_START.md) | **Instant overview** (30 seconds) |
| [Documentation Index](DOCUMENTATION_INDEX.md) | Finding anything quickly |
| [Onboarding](AI_AGENT_ONBOARDING.md) | First-time setup, understanding basics |
| [Quick Reference](AI_QUICK_REFERENCE.md) | Templates, commands, quick lookups |
| [Architecture](ARCHITECTURE.md) | Understanding system design, diagrams |
| [Decision Tree](DECISION_TREE.md) | Making implementation decisions |
| [Guidelines](AI_AGENT_GUIDELINES.md) | Detailed patterns, conventions, deep dive |

## 📁 Project Structure

* `/composeApp` - Main multiplatform application entry point
  - `commonMain` - Code shared across all platforms
  - `androidMain` - Android-specific implementations
  - `iosMain` - iOS-specific implementations
  - `wasmJsMain` - Web-specific implementations

* `/sharedFrontend` - Modular shared frontend code
  - `ui-*` - Feature-based UI modules
  - `navigation` - Navigation logic and destinations
  - `data` - Repository implementations
  - `domain` - Business logic and use cases
  - `network` - HTTP client configuration
  - `common-model` - Shared data models
  - `common-resources` - Shared UI resources (theme, colors)

* `/iosApp` - iOS application wrapper and SwiftUI integration
  - Entry point for iOS app
  - Contains native iOS code and configurations

* `/server` - Ktor backend server
  - REST API implementation
  - Database layer with Exposed ORM

* `/shared` - Code shared between client and server
  - Common models and utilities

* `/build-logic` - Custom Gradle convention plugins
  - Centralized build configuration
  - Platform target configuration

## 🛠️ Technology Stack

- **Kotlin**: 2.2.21
- **Compose Multiplatform**: 1.9.0 (Shared UI framework)
- **Ktor**: 3.3.3 (HTTP client & server)
- **Koin**: 4.1.0 (Dependency injection)
- **Kotlinx Serialization**: 1.9.0 (JSON serialization)
- **Kotlinx Coroutines**: 1.10.1 (Asynchronous programming)
- **Exposed**: 0.53.0 (Database ORM)
- **Firebase GitLive**: Crashlytics, Analytics, Remote Config

## ✨ Key Features

- **Clean Architecture** with clear separation of concerns
- **MVI Pattern** for predictable state management
- **Modular Design** for scalability and maintainability
- **Type-Safe Navigation** with Compose Navigation
- **Centralized DI** with Koin
- **Platform Abstractions** using expect/actual
- **Code Quality Tools** (ktlint, detekt)
- **Firebase Integration** for analytics and crash reporting

## 🚀 Getting Started

### Prerequisites

- JDK 17 or higher
- Android Studio Ladybug or later (for Android)
- Xcode 15+ (for iOS)
- Node.js (for Web)

### Building the Project

```bash
# Build all platforms
./gradlew build

# Run Android app
./gradlew :composeApp:assembleDebug

# Run iOS app (generate framework first)
./gradlew :composeApp:linkDebugFrameworkIosArm64
# Then open iosApp/iosApp.xcworkspace in Xcode

# Run Web app
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# Run Server
./gradlew :server:run
```

### Code Quality Checks

```bash
# Run ktlint
./gradlew ktlintCheck

# Auto-fix formatting issues
./gradlew ktlintFormat

# Run detekt
./gradlew detekt
```

## 🏗️ Architecture

The project follows **Clean Architecture** principles:

```
UI Layer (Compose) → Domain Layer (Use Cases) → Data Layer (Repositories) → Network/Database
```

Each feature module includes:
- **Screen**: Composable UI
- **ViewModel**: State management and business logic
- **State**: Immutable UI state
- **UiAction**: User interaction events
- **Navigation**: Type-safe routing

## 📖 Development Guidelines

### Creating a New Feature

1. Create feature module in `sharedFrontend/ui-myfeature/`
2. Add module to `settings.gradle.kts`
3. Implement Screen, ViewModel, State, and UiAction
4. Register ViewModel in Koin
5. Add navigation destination and graph
6. Write tests

See [AI Quick Reference](AI_QUICK_REFERENCE.md) for detailed templates and examples.

## 🧪 Testing

```bash
# Run all tests
./gradlew test

# Run Android tests
./gradlew :composeApp:testDebugUnitTest

# Run common tests
./gradlew :sharedFrontend:commonTest
```

## 📦 Dependency Management

Dependencies are managed via Gradle version catalog in `gradle/libs.versions.toml`.

To add a new dependency:
1. Add version to `[versions]` section
2. Add library to `[libraries]` section
3. Reference in module's `build.gradle.kts`

## 🔧 Configuration

- **Android Config**: `build-logic/convention/src/.../AndroidConfigPlugin.kt`
  - Min SDK: 24, Target SDK: 35, Compile SDK: 35
  
- **KMP Targets**: `build-logic/convention/src/.../TargetConfigPlugin.kt`
  - Android, iOS (x64, arm64, simulator), WasmJS

- **Detekt Rules**: `config/detekt/detekt.yml`

- **ProGuard Rules**: `composeApp/proguard-rules.pro`

## 🌐 Platform-Specific Notes

### Android
- Google Services configured for Firebase
- ProGuard rules for release builds
- Material Design 3 theming

### iOS
- Static framework export
- CocoaPods integration
- SwiftPM dependencies support

### Web (WasmJS)
- Run task: `:composeApp:wasmJsBrowserDevelopmentRun`
- Browser-based development mode

### Server
- Ktor with Netty engine
- PostgreSQL database
- Flyway migrations

## 📝 License

[Add your license here]

## 🤝 Contributing

[Add contribution guidelines here]
## 📚 Learn More

### Official Resources
- [Kotlin Multiplatform Documentation](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform)
- [Kotlin/Wasm](https://kotl.in/wasm/)
- [Ktor Documentation](https://ktor.io/docs/)
- [Koin Documentation](https://insert-koin.io/)

### Community
- [Kotlin Slack](https://slack-chats.kotlinlang.org/) - #compose-web channel
- [GitHub Issues](https://github.com/JetBrains/compose-multiplatform/issues) - Report issues

### Project Documentation
- [AI Agent Guidelines](AI_AGENT_GUIDELINES.md) - Complete development guide
- [Quick Reference](AI_QUICK_REFERENCE.md) - Cheat sheet for common tasks
