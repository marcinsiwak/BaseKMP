# AI Agent Guidelines for BaseKMP Project

> **Last Updated**: February 16, 2026  
> **Project Type**: Kotlin Multiplatform (KMP) with Compose Multiplatform  
> **Supported Platforms**: Android, iOS, Web (WasmJS), Server (Ktor)

---

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture & Design Patterns](#architecture--design-patterns)
3. [Project Structure](#project-structure)
4. [Technology Stack](#technology-stack)
5. [Code Style & Conventions](#code-style--conventions)
6. [Module Guidelines](#module-guidelines)
7. [Platform-Specific Guidelines](#platform-specific-guidelines)
8. [Dependency Management](#dependency-management)
9. [Testing Strategy](#testing-strategy)
10. [Build & Configuration](#build--configuration)
11. [Common Patterns & Best Practices](#common-patterns--best-practices)
12. [Do's and Don'ts](#dos-and-donts)

---

## 🎯 Project Overview

BaseKMP is a production-ready Kotlin Multiplatform template featuring:
- **Multi-platform support**: Android, iOS, Web (WasmJS), and Backend (Ktor)
- **Compose Multiplatform** for shared UI
- **Clean Architecture** with clear separation of concerns
- **Modular design** for better scalability and maintainability
- **Koin** for dependency injection
- **Ktor** for networking (client) and backend server
- **Firebase integration** (Crashlytics, Analytics, Remote Config)

---

## 🏗️ Architecture & Design Patterns

### Core Architecture
The project follows **Clean Architecture** principles with clear layer separation:

```
Presentation Layer (UI) → Domain Layer (Business Logic) → Data Layer (Repository) → Network/Database
```

### Key Patterns

#### 1. **MVI (Model-View-Intent) Pattern**
- **ViewModel**: Manages UI state and business logic
- **State**: Immutable data class representing UI state
- **UiAction**: Sealed class/interface for user interactions

**Example Structure**:
```kotlin
// State
data class ExampleState(
    val players: List<Player> = emptyList(),
    val isLoading: Boolean = false
)

// UI Actions
sealed interface ExampleUiAction {
    data class PlayerPicked(val pos: Int) : ExampleUiAction
    object CreateExample : ExampleUiAction
}

// ViewModel
class ExampleViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(ExampleState())
    val viewState = _viewState.asStateFlow()
    
    fun onUiAction(action: ExampleUiAction) {
        when (action) {
            is ExampleUiAction.PlayerPicked -> handlePlayerPicked(action.pos)
            ExampleUiAction.CreateExample -> handleCreate()
        }
    }
}
```

#### 2. **Navigation Pattern**
- Uses **Jetpack Compose Navigation** with type-safe routing
- Custom `Navigator` class for centralized navigation logic
- Navigation events via SharedFlow

```kotlin
class Navigator {
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()
    
    suspend fun navigate(navDestination: NavDestination) {
        _navigationEvent.emit(NavigationEvent.NavigateToDestination(navDestination))
    }
}
```

#### 3. **Dependency Injection with Koin**
- Modular DI configuration
- Separate modules for different concerns (ViewModel, UseCase, Repository, Network)
- Use `koinInject()` in Composables

**Module Pattern**:
```kotlin
val viewModelModule = module {
    viewModel { ExampleViewModel() }
}

val appModule = listOf(
    navigationModule,
    viewModelModule,
    useCaseModule,
    repositoryModule,
    networkModule
)
```

#### 4. **Repository Pattern**
- Abstracts data sources
- Lives in `data` module
- Coordinates between network, database, and cache

---

## 📁 Project Structure

### Root Modules

```
BaseKMP/
├── composeApp/              # Main multiplatform application
├── sharedFrontend/          # Shared frontend logic and UI
├── server/                  # Ktor backend server
├── shared/                  # Shared models between client/server
├── build-logic/             # Custom Gradle convention plugins
├── config/                  # Configuration files (detekt, git-hooks)
└── iosApp/                  # iOS native wrapper
```

### sharedFrontend Module Structure

The `sharedFrontend` module is organized into feature-based submodules:

```
sharedFrontend/
├── buildConfig/             # Build configuration
├── common-model/            # Shared data models
├── common-resources/        # Shared resources (theme, colors, strings)
├── data/                    # Data layer (repositories)
├── domain/                  # Domain layer (business logic, use cases)
├── domain-impl/             # Domain implementations
├── global-loader-manager/   # Global loading state management
├── navigation/              # Navigation logic and destinations
├── network/                 # Network client setup
├── remote-config/           # Firebase Remote Config
├── ui-example/              # Example feature UI
└── ui-ai-generated/         # AI-generated feature UI
```

### Source Set Organization

Each multiplatform module follows this structure:
```
src/
├── androidMain/             # Android-specific code
├── commonMain/              # Shared code across all platforms
├── iosMain/                 # iOS-specific code
├── jvmMain/                 # JVM-specific code (optional)
└── wasmJsMain/              # Web-specific code (optional)
```

---

## 🛠️ Technology Stack

### Core Technologies
| Technology | Version | Purpose |
|------------|---------|---------|
| Kotlin | 2.2.21 | Primary language |
| Compose Multiplatform | 1.9.0 | UI framework |
| Kotlin Serialization | 1.9.0 | JSON serialization |
| Coroutines | 1.10.1 | Asynchronous programming |
| Ktor | 3.3.3 | HTTP client & server |
| Koin | 4.1.0-Beta1 | Dependency injection |

### Android
- **Min SDK**: 24
- **Target SDK**: 35
- **Compile SDK**: 35
- **JVM Target**: 17

### iOS
- Targets: `iosX64`, `iosArm64`, `iosSimulatorArm64`
- Framework: Static framework export

### Additional Libraries
- **Compose Navigation**: `2.9.0` - Type-safe navigation
- **Exposed**: `0.53.0` - Database ORM (server)
- **PostgreSQL**: `42.7.2` - Database driver (server)
- **Firebase GitLive**: `2.4.0` - Crashlytics, Analytics, Remote Config
- **Compottie**: `2.0.0` - Lottie animations
- **Kotlinx DateTime**: `0.7.1` - Date/time handling

---

## 📝 Code Style & Conventions

### Kotlin Style Guide

#### 1. **Naming Conventions**
- **Classes/Interfaces**: PascalCase (`ExampleViewModel`, `Player`)
- **Functions/Properties**: camelCase (`onUiAction`, `viewState`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_RETRY_COUNT`)
- **Packages**: lowercase (`pl.msiwak.ui.example`)

#### 2. **File Organization**
- One top-level class per file
- File name matches the class name
- Group related classes in the same directory

#### 3. **Immutability**
```kotlin
// ✅ Prefer val over var
val viewState = _viewState.asStateFlow()

// ✅ Use immutable collections
val players: List<Player> = emptyList()

// ✅ Use data classes for state
data class ExampleState(
    val isLoading: Boolean = false,
    val data: String? = null
)
```

#### 4. **Null Safety**
```kotlin
// ✅ Use safe calls and elvis operator
val name = user?.name ?: "Unknown"

// ✅ Prefer non-nullable types
fun processData(data: String) // Better than String?

// ✅ Use requireNotNull with meaningful messages
val user = userRepo.getUser() ?: error("User not found")
```

#### 5. **Function Design**
```kotlin
// ✅ Single responsibility
fun validateEmail(email: String): Boolean

// ✅ Extension functions for utility methods
fun String.isValidEmail(): Boolean

// ✅ Suspend functions for async operations
suspend fun fetchData(): Result<Data>
```

#### 6. **Coroutines**
```kotlin
// ✅ Use viewModelScope in ViewModels
viewModelScope.launch {
    // Safe coroutine execution
}

// ✅ Handle exceptions properly
runCatching {
    fetchData()
}.onSuccess { data ->
    // Handle success
}.onFailure { error ->
    // Handle error
}
```

### Linting & Static Analysis

The project uses:
- **ktlint** (`11.5.1`) - Code formatting
- **detekt** (`1.23.8`) - Static analysis

**Configuration**:
- ktlint config: Applied via plugin
- detekt config: `/config/detekt/detekt.yml`

**Exclusions**:
- Generated code directories
- Buildkonfig generated files
- `main.kt` entry points

---

## 🧩 Module Guidelines

### Creating New Feature Modules

#### 1. **UI Feature Module** (e.g., `ui-new-feature`)

**build.gradle.kts**:
```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("pl.msiwak.convention.android.config")
    id("pl.msiwak.convention.target.config")
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ui-new-feature"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedFrontend.commonModel)
            implementation(projects.sharedFrontend.commonResources)
            
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
        }
    }
}

android {
    namespace = "pl.msiwak.basekmp.ui.newfeature"
}
```

**Required files**:
```
ui-new-feature/
├── build.gradle.kts
└── src/
    └── commonMain/
        └── kotlin/
            └── pl/msiwak/ui/newfeature/
                ├── NewFeatureScreen.kt      # Composable UI
                ├── NewFeatureViewModel.kt   # Business logic
                ├── NewFeatureState.kt       # UI state
                ├── NewFeatureUiAction.kt    # User actions
                └── graph/
                    └── NewFeatureGraph.kt   # Navigation graph
```

#### 2. **Data/Repository Module**

**Structure**:
```
data/
└── src/
    └── commonMain/
        └── kotlin/
            └── pl/msiwak/data/
                ├── repository/
                │   └── ExampleRepositoryImpl.kt
                └── datasource/
                    ├── local/
                    └── remote/
```

Dependencies:
```kotlin
commonMain.dependencies {
    implementation(projects.sharedFrontend.network)
    implementation(projects.sharedFrontend.commonModel)
    implementation(libs.kotlinx.coroutines)
}
```

#### 3. **Domain Module**

For use cases and business logic interfaces:
```kotlin
// Domain interface
interface GetUserUseCase {
    suspend operator fun invoke(userId: String): Result<User>
}

// Implementation in domain-impl
class GetUserUseCaseImpl(
    private val repository: UserRepository
) : GetUserUseCase {
    override suspend fun invoke(userId: String): Result<User> {
        return repository.getUser(userId)
    }
}
```

### Module Dependencies Rule

**Allowed dependency flow**:
```
UI → Domain → Data → Network
     ↓         ↓
  Common Model
```

**Never**:
- Data → UI
- Domain → UI
- Network → Domain/UI

---

## 🎨 Platform-Specific Guidelines

### expect/actual Pattern

Use `expect`/`actual` for platform-specific implementations.

**Common code (commonMain)**:
```kotlin
expect class EngineProvider() {
    fun getEngine(): HttpClientEngineFactory<HttpClientEngineConfig>
}

expect object Dispatchers {
    val io: CoroutineDispatcher
}

expect object AppContext
```

**Android implementation (androidMain)**:
```kotlin
actual class EngineProvider {
    actual fun getEngine() = Android
}

actual object Dispatchers {
    actual val io: CoroutineDispatcher = kotlinx.coroutines.Dispatchers.IO
}

actual object AppContext {
    lateinit var context: Context
}
```

**iOS implementation (iosMain)**:
```kotlin
actual class EngineProvider {
    actual fun getEngine() = Darwin
}

actual object Dispatchers {
    actual val io: CoroutineDispatcher = kotlinx.coroutines.Dispatchers.Default
}

actual object AppContext
```

### Android-Specific

#### ProGuard Rules
Located: `/composeApp/proguard-rules.pro`

Key rules:
- Keep kotlinx.serialization metadata
- Keep Ktor debug utilities
- Preserve companion objects of serializable classes

#### Firebase Integration
- `google-services.json` in `composeApp/`
- Plugins: `google-services`, `firebase-crashlytics`

```kotlin
// In commonMain, use GitLive Firebase
implementation(libs.firebase.gitlive.crashlytics)
implementation(libs.firebase.gitlive.analytics)
implementation(libs.firebase.gitlive.remoteConfig)
```

### iOS-Specific

#### Framework Export
```kotlin
iosTarget.binaries.framework {
    baseName = "ComposeApp"
    isStatic = true
    export(projects.sharedFrontend.commonModel)
}
```

#### CocoaPods Integration
- Podspec: `composeApp.podspec`
- Swift integration via Xcode project

### Web (WasmJS)

Run task: `:composeApp:wasmJsBrowserDevelopmentRun`

Target configuration in TargetConfigPlugin:
```kotlin
wasmJs {
    browser()
}
```

---

## 📦 Dependency Management

### Version Catalog

All dependencies managed in `/gradle/libs.versions.toml`

**Adding a new dependency**:

1. Add version:
```toml
[versions]
my-library = "1.0.0"
```

2. Add library:
```toml
[libraries]
my-library = { module = "com.example:my-library", version.ref = "my-library" }
```

3. Use in module:
```kotlin
commonMain.dependencies {
    implementation(libs.my.library)
}
```

### Dependency Locking

Project uses Gradle dependency locking:
```kotlin
dependencyLocking {
    lockAllConfigurations()
}
```

Update locks: `./gradlew dependencies --write-locks`

### Common Dependencies by Layer

**UI Layer**:
```kotlin
implementation(compose.runtime)
implementation(compose.foundation)
implementation(compose.material)
implementation(compose.ui)
implementation(libs.koin.compose)
implementation(libs.compose.navigation)
```

**ViewModel Layer**:
```kotlin
implementation(libs.koin.core)
implementation(libs.koin.compose.viewModel)
implementation(libs.kotlinx.coroutines)
```

**Data/Repository Layer**:
```kotlin
implementation(libs.kotlinx.coroutines)
implementation(libs.kotlinx.serialization)
implementation(projects.sharedFrontend.network)
```

**Network Layer**:
```kotlin
implementation(libs.ktor.core)
implementation(libs.ktor.contentNegation)
implementation(libs.ktor.serialization)
implementation(libs.ktor.logger)
// Platform-specific engines
androidMain.dependencies {
    implementation(libs.ktor.android)
}
iosMain.dependencies {
    implementation(libs.ktor.ios)
}
```

---

## 🧪 Testing Strategy

### Test Organization

```
src/
├── commonTest/          # Shared tests across platforms
├── androidUnitTest/     # Android unit tests
├── androidInstrumentedTest/  # Android instrumented tests
└── iosTest/             # iOS-specific tests
```

### Testing Dependencies

```kotlin
commonTest.dependencies {
    implementation(libs.kotlin.test)
    implementation(libs.koin.test)
    implementation(libs.kotlinx.coroutines.test)
}

androidUnitTest.dependencies {
    implementation(libs.kotlin.test.junit)
    implementation(libs.junit)
}
```

### Testing ViewModels

```kotlin
@Test
fun `test state updates correctly`() = runTest {
    val viewModel = ExampleViewModel()
    
    viewModel.onUiAction(ExampleUiAction.CreateExample)
    
    val state = viewModel.viewState.value
    assertTrue(state.isLoading)
}
```

### Testing with Koin

```kotlin
class MyTest : KoinTest {
    @Before
    fun setup() {
        startKoin {
            modules(testModule)
        }
    }
    
    @After
    fun teardown() {
        stopKoin()
    }
}
```

---

## ⚙️ Build & Configuration

### Gradle Convention Plugins

Located in `/build-logic/convention/`

#### 1. **AndroidConfigPlugin** (`pl.msiwak.convention.android.config`)
Configures:
- Compile SDK: 35
- Min SDK: 24
- Target SDK: 35
- Java version: 17
- Packaging options

#### 2. **TargetConfigPlugin** (`pl.msiwak.convention.target.config`)
Configures KMP targets:
- Android
- iOS (x64, arm64, simulatorArm64)
- JVM toolchain 17

#### 3. **ReleaseOnlyBuildVariantPlugin** (`pl.msiwak.convention.releaseonly.config`)
Optimizes build for release-only configurations

### Using Convention Plugins

In module's `build.gradle.kts`:
```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("pl.msiwak.convention.android.config")
    id("pl.msiwak.convention.target.config")
}
```

### Build Configuration

#### Gradle Properties
`/gradle.properties`:
```properties
kotlin.code.style=official
kotlin.daemon.jvmargs=-Xmx2048M
org.gradle.jvmargs=-Xmx6144M
android.useAndroidX=true
```

#### Version Management
In `/composeApp/build.gradle.kts`:
```kotlin
val versionMajor = 1
val versionMinor = 0
val versionPatch = 3
val versionBuild = 0
val versionCode = 1_000_000 * versionMajor + 10_000 * versionMinor + 100 * versionPatch + versionBuild
```

### Build Tasks

```bash
# Build all platforms
./gradlew build

# Android tasks
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:assembleRelease

# iOS tasks
./gradlew :composeApp:linkDebugFrameworkIosArm64

# Web tasks
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# Server tasks
./gradlew :server:run

# Code quality
./gradlew ktlintCheck
./gradlew detekt
```

---

## 💡 Common Patterns & Best Practices

### 1. **State Management**

```kotlin
// ✅ Use StateFlow for state
class MyViewModel : ViewModel() {
    private val _state = MutableStateFlow(MyState())
    val state = _state.asStateFlow()
    
    fun updateState(newValue: String) {
        _state.update { it.copy(value = newValue) }
    }
}

// ✅ Collect state in Composables
@Composable
fun MyScreen(viewModel: MyViewModel = koinInject()) {
    val state by viewModel.state.collectAsState()
    
    Text(state.value)
}
```

### 2. **Error Handling**

```kotlin
// ✅ Use Result for operations that can fail
suspend fun fetchData(): Result<Data> = runCatching {
    api.getData()
}.mapCatching { response ->
    response.toData()
}

// ✅ Handle errors at appropriate level
viewModelScope.launch {
    fetchData()
        .onSuccess { data -> _state.update { it.copy(data = data) } }
        .onFailure { error -> _state.update { it.copy(error = error.message) } }
}
```

### 3. **Resource Management**

```kotlin
// ✅ Use resources from common-resources module
MaterialTheme(colors = LightColorScheme) {
    // UI content
}

// ✅ Compose resources for strings/images
implementation(compose.components.resources)
```

### 4. **Navigation**

```kotlin
// ✅ Define destinations as serializable objects
@Serializable
data class ExampleDestination(val id: String) : NavDestination

// ✅ Navigate using Navigator
suspend fun navigateToExample(id: String) {
    navigator.navigate(ExampleDestination(id))
}

// ✅ Create navigation graphs
fun NavGraphBuilder.exampleGraph() {
    composable<ExampleDestination> { backStackEntry ->
        val args = backStackEntry.toRoute<ExampleDestination>()
        ExampleScreen()
    }
}
```

### 5. **Loading States**

```kotlin
// ✅ Use global loader manager
implementation(projects.sharedFrontend.globalLoaderManager)

// Show loader in UI
if (viewState.value.isLoading) {
    GlobalLoader(modifier = Modifier.align(Center))
}
```

### 6. **Ktor Client Setup**

```kotlin
// ✅ Platform-specific engines via expect/actual
val client = HttpClient(EngineProvider().getEngine()) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(Logging) {
        level = LogLevel.INFO
    }
}
```

### 7. **Serialization**

```kotlin
// ✅ Use kotlinx.serialization
@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String? = null
)

// ✅ Plugin in module
plugins {
    alias(libs.plugins.serialization)
}
```

### 8. **Compose Best Practices**

```kotlin
// ✅ Separate composables
@Composable
fun MyScreen(viewModel: MyViewModel = koinInject()) {
    MyScreenContent(
        state = viewModel.state.collectAsState().value,
        onAction = viewModel::onAction
    )
}

@Composable
fun MyScreenContent(state: MyState, onAction: (MyAction) -> Unit) {
    // UI without ViewModel dependency
}

// ✅ Use remember for expensive calculations
val expensiveValue = remember(key) {
    computeExpensiveValue()
}

// ✅ Use LaunchedEffect for side effects
LaunchedEffect(key1) {
    // Side effect
}
```

---

## 📖 Documentation Guidelines

### README Files

**IMPORTANT**: This project uses a **single root README.md** file for all project documentation.

✅ **Do:**
- Update the root `/README.md` for project-level documentation
- Add module descriptions to this guidelines document or architecture docs
- Use inline code comments for complex logic
- Document public APIs with KDoc
- Keep documentation in `.github/agents/` folder for AI agent guidance

❌ **Don't:**
- Create README.md files in module directories (e.g., `sharedFrontend/ui-feature/README.md`)
- Create additional README files in subdirectories
- Duplicate project documentation across multiple files
- Create per-module documentation files

**Rationale**: A single README keeps documentation consistent, avoids duplication, and provides a central source of truth for the project. Module-specific details belong in code comments or the architecture documentation.

---

## ✅ Do's and ❌ Don'ts

### General

✅ **Do:**
- Use immutable data structures
- Prefer composition over inheritance
- Write descriptive variable and function names
- Use Kotlin's standard library functions (`map`, `filter`, `fold`, etc.)
- Document public APIs with KDoc
- Handle errors explicitly
- Use coroutines for async operations

❌ **Don't:**
- Use `!!` (non-null assertion) - use safe calls or requireNotNull
- Create circular dependencies between modules
- Mix platform-specific code in `commonMain`
- Ignore compiler warnings
- Use `Any` type without good reason
- Block the main thread
- Catch generic `Exception` without re-throwing or logging
- Create README files in module directories - use only the root `/README.md`

### Multiplatform Specific

✅ **Do:**
- Use `expect`/`actual` for platform abstractions
- Test on all target platforms
- Keep common code truly platform-agnostic
- Use KMP-compatible libraries when available
- Document platform-specific behavior

❌ **Don't:**
- Access platform-specific APIs in common code
- Assume platform availability (check at runtime if needed)
- Use JVM-only libraries in common code
- Forget to implement `actual` for all targets
- Mix Android/iOS SDKs in shared modules

### UI/Compose

✅ **Do:**
- Use `remember` for state that survives recomposition
- Use `LaunchedEffect` for side effects
- Extract reusable composables
- Use `Modifier` for styling
- Follow Material Design guidelines
- Make composables testable (separate state/behavior)

❌ **Don't:**
- Perform heavy computations in composables
- Make network calls directly in composables
- Create unnecessary recompositions
- Use hardcoded strings (use resources)
- Ignore accessibility

### Architecture

✅ **Do:**
- Follow single responsibility principle
- Keep ViewModels platform-agnostic
- Use interfaces for abstractions
- Inject dependencies via Koin
- Separate concerns (UI, domain, data)
- Make code testable

❌ **Don't:**
- Put business logic in composables
- Create god classes/objects
- Tightly couple layers
- Access repositories directly from UI
- Skip error handling in repositories
- Make ViewModels aware of Android/iOS specifics

### Dependencies

✅ **Do:**
- Use version catalog for dependency management
- Keep dependencies up to date
- Use KMP-compatible libraries
- Minimize transitive dependencies
- Lock dependency versions for reproducible builds

❌ **Don't:**
- Add platform-specific dependencies to common code
- Use multiple libraries for the same purpose
- Add dependencies without reviewing licenses
- Ignore security vulnerabilities
- Use snapshots in production

---

## 🔧 Troubleshooting Common Issues

### Build Issues

**Issue**: "Cannot find symbol" errors in Android
- **Solution**: Run `./gradlew clean build` or invalidate caches in IDE

**Issue**: iOS framework linking errors
- **Solution**: Clean DerivedData folder and rebuild from Xcode

**Issue**: Kotlin version mismatch
- **Solution**: Ensure all Kotlin plugins use the same version (check constraints in root build.gradle.kts)

### Runtime Issues

**Issue**: Koin injection failures
- **Solution**: Verify module is included in `appModule` list and correct scope is used

**Issue**: Serialization exceptions
- **Solution**: Add `@Serializable` annotation and check ProGuard rules

**Issue**: Navigation not working
- **Solution**: Ensure NavHost is collecting navigation events and destinations are registered

---

## 📚 Additional Resources

### Official Documentation
- [Kotlin Multiplatform Docs](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Ktor Documentation](https://ktor.io/docs/)
- [Koin Documentation](https://insert-koin.io/)

### Project-Specific
- Main README: `/README.md`
- Detekt Config: `/config/detekt/detekt.yml`
- Version Catalog: `/gradle/libs.versions.toml`

---

## 🎯 Quick Reference Checklist

When implementing a new feature:

- [ ] Create feature module in appropriate location
- [ ] Add module to `settings.gradle.kts`
- [ ] Configure `build.gradle.kts` with convention plugins
- [ ] Define State, UiAction, and ViewModel
- [ ] Create Composable UI
- [ ] Add Koin module for dependencies
- [ ] Create navigation destination and graph
- [ ] Write unit tests
- [ ] Run ktlint and detekt
- [ ] Test on all target platforms
- [ ] Update documentation if needed

---

## 📝 Notes for AI Agents

1. **Always check existing patterns** before implementing new solutions
2. **Respect module boundaries** - don't create circular dependencies
3. **Use convention plugins** for consistent configuration
4. **Follow MVI pattern** for UI features
5. **Platform-specific code** goes in `androidMain`/`iosMain`, not `commonMain`
6. **Update version catalog** for new dependencies
7. **Run code quality checks** before committing
8. **Consider all platforms** when making architectural decisions
9. **Document significant decisions** with comments
10. **Keep the guidelines updated** as the project evolves

---

**End of Guidelines Document**
