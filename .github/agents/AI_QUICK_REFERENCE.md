# AI Quick Reference Guide - BaseKMP

> **One-page cheat sheet for common operations**

## 🏗️ Project Structure Quick Map

```
composeApp/          → Main app entry point
sharedFrontend/      → Shared UI & logic
  ├── ui-*/          → Feature UI modules
  ├── navigation/    → Navigation logic
  ├── data/          → Repositories
  ├── domain/        → Use cases
  ├── network/       → HTTP client
  └── common-*/      → Shared models/resources
server/              → Ktor backend
build-logic/         → Gradle plugins
```

## 🎯 Architecture Pattern

```
UI (Compose) → ViewModel → UseCase → Repository → Network/DB
     ↓             ↓           ↓           ↓
   Screen      State       Domain       Data
```

## 🔧 Common Commands

```bash
# Build
./gradlew build

# Android
./gradlew :composeApp:assembleDebug

# iOS Framework
./gradlew :composeApp:linkDebugFrameworkIosArm64

# Web
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# Code Quality
./gradlew ktlintCheck detekt
./gradlew ktlintFormat  # Auto-fix formatting
```

## 📝 Creating a New Feature

### 1. Create Module Structure
```
ui-myfeature/
└── src/commonMain/kotlin/pl/msiwak/ui/myfeature/
    ├── MyFeatureScreen.kt
    ├── MyFeatureViewModel.kt
    ├── MyFeatureState.kt
    ├── MyFeatureUiAction.kt
    └── graph/MyFeatureGraph.kt
```

### 2. build.gradle.kts Template
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
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework { baseName = "ui-myfeature" }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedFrontend.commonModel)
            implementation(compose.runtime)
            implementation(compose.material)
            implementation(libs.koin.compose)
        }
    }
}

android { namespace = "pl.msiwak.basekmp.ui.myfeature" }
```

### 3. ViewModel Template
```kotlin
class MyFeatureViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(MyFeatureState())
    val viewState = _viewState.asStateFlow()
    
    fun onUiAction(action: MyFeatureUiAction) {
        when (action) {
            is MyFeatureUiAction.ButtonClicked -> handleClick()
        }
    }
}
```

### 4. State & Action Templates
```kotlin
data class MyFeatureState(
    val isLoading: Boolean = false,
    val data: List<Item> = emptyList(),
    val error: String? = null
)

sealed interface MyFeatureUiAction {
    object ButtonClicked : MyFeatureUiAction
    data class ItemSelected(val id: String) : MyFeatureUiAction
}
```

### 5. Screen Template
```kotlin
@Composable
fun MyFeatureScreen(viewModel: MyFeatureViewModel = koinInject()) {
    val state by viewModel.viewState.collectAsState()
    MyFeatureContent(state, viewModel::onUiAction)
}

@Composable
fun MyFeatureContent(
    state: MyFeatureState,
    onAction: (MyFeatureUiAction) -> Unit
) {
    Scaffold {
        // UI here
    }
}
```

### 6. Register in Koin
```kotlin
// In ViewModelModule.kt
viewModel { MyFeatureViewModel() }
```

### 7. Add Navigation
```kotlin
// Define destination
@Serializable
object MyFeatureDestination : NavDestination

// Add to navigation graph
fun NavGraphBuilder.myFeatureGraph() {
    composable<MyFeatureDestination> {
        MyFeatureScreen()
    }
}
```

## 🔌 Common Dependencies by Layer

### UI Module
```kotlin
implementation(compose.runtime)
implementation(compose.foundation)
implementation(compose.material)
implementation(libs.koin.compose)
implementation(libs.compose.navigation)
```

### Data Module
```kotlin
implementation(projects.sharedFrontend.network)
implementation(projects.sharedFrontend.commonModel)
implementation(libs.kotlinx.coroutines)
```

### Network Module
```kotlin
implementation(libs.ktor.core)
implementation(libs.ktor.contentNegation)
implementation(libs.ktor.serialization)
```

## 🎨 Platform-Specific Code

### expect/actual Pattern
```kotlin
// commonMain
expect object Dispatchers {
    val io: CoroutineDispatcher
}

// androidMain
actual object Dispatchers {
    actual val io = kotlinx.coroutines.Dispatchers.IO
}

// iosMain
actual object Dispatchers {
    actual val io = kotlinx.coroutines.Dispatchers.Default
}
```

## 📦 Adding New Dependency

### 1. Update libs.versions.toml
```toml
[versions]
my-lib = "1.0.0"

[libraries]
my-lib = { module = "com.example:my-lib", version.ref = "my-lib" }
```

### 2. Use in build.gradle.kts
```kotlin
commonMain.dependencies {
    implementation(libs.my.lib)
}
```

## 🧪 Testing Patterns

```kotlin
class MyViewModelTest {
    @Test
    fun `test state updates`() = runTest {
        val vm = MyViewModel()
        vm.onUiAction(MyAction.Click)
        assertEquals(expected, vm.viewState.value)
    }
}
```

## ⚙️ Build Configuration

### Key Files
- **Versions**: `gradle/libs.versions.toml`
- **Modules**: `settings.gradle.kts`
- **Android Config**: Min 24, Target 35, Java 17
- **iOS Targets**: x64, arm64, simulatorArm64

## 🚨 Common Mistakes to Avoid

❌ **DON'T**:
- Use `!!` operator
- Put platform code in `commonMain`
- Access ViewModel from another ViewModel
- Make network calls in Composables
- Use mutable state without StateFlow
- Create circular module dependencies
- Create README files in modules (use only root `/README.md`)

✅ **DO**:
- Use safe calls `?.` or `requireNotNull()`
- Use `expect`/`actual` for platform code
- Use Navigator for navigation
- Make API calls in Repository/ViewModel
- Use StateFlow for reactive state
- Follow dependency flow: UI → Domain → Data
- Update only the root README for documentation

## 🔍 Code Style Checklist

- [ ] Use `val` over `var`
- [ ] Immutable data classes for state
- [ ] Suspend functions for async ops
- [ ] `viewModelScope.launch` for coroutines
- [ ] `runCatching` for error handling
- [ ] KDoc for public APIs
- [ ] Descriptive names (no abbreviations)

## 🎯 Target Platforms

- **Android**: SDK 24-35
- **iOS**: x64, arm64, simulatorArm64
- **Web**: WasmJS
- **Server**: JVM (Ktor)

## 📋 Pre-Commit Checklist

- [ ] Code builds successfully
- [ ] `./gradlew ktlintCheck` passes
- [ ] `./gradlew detekt` passes
- [ ] Tests added/updated
- [ ] Tested on target platforms
- [ ] No hardcoded strings
- [ ] No platform-specific code in common
- [ ] No module README files created (use root only)

## 🔗 Quick Links

- Full Guidelines: `AI_AGENT_GUIDELINES.md`
- Version Catalog: `gradle/libs.versions.toml`
- Detekt Config: `config/detekt/detekt.yml`
- Main README: `README.md`

---

**Last Updated**: February 16, 2026
