# 🤖 AI Agent Onboarding - BaseKMP

**Quick Start Guide for AI Agents Working on This Project**

## 📋 First Things First

This is a **Kotlin Multiplatform (KMP)** project with:
- **Platforms**: Android, iOS, Web (WasmJS), Server (Ktor)
- **UI Framework**: Compose Multiplatform
- **Architecture**: Clean Architecture + MVI Pattern
- **DI**: Koin
- **Networking**: Ktor

## 📚 Essential Reading (in order)

1. **Start here**: [AI_QUICK_REFERENCE.md](AI_QUICK_REFERENCE.md) - One-page cheat sheet
2. **Then read**: [ARCHITECTURE.md](ARCHITECTURE.md) - Visual architecture diagrams
3. **Deep dive**: [AI_AGENT_GUIDELINES.md](AI_AGENT_GUIDELINES.md) - Complete guidelines
4. **Project info**: [README.md](README.md) - Project overview

## 🎯 Core Concepts (Must Know)

### 1. Module Organization
```
composeApp/          → App entry point (Android, iOS, Web)
sharedFrontend/      → Shared frontend code
  ├── ui-*/          → Feature modules (UI + ViewModels)
  ├── data/          → Repositories
  ├── domain/        → Business logic
  └── network/       → HTTP client
server/              → Backend (Ktor)
```

### 2. Architecture Pattern (MVI)
```kotlin
Screen → UiAction → ViewModel → State → Screen
                        ↓
                    UseCase
                        ↓
                    Repository
                        ↓
                      Network
```

### 3. File Structure per Feature
Every feature module has:
- `FeatureScreen.kt` - Composable UI
- `FeatureViewModel.kt` - State management
- `FeatureState.kt` - Data class for UI state
- `FeatureUiAction.kt` - Sealed interface for actions

### 4. Platform-Specific Code
Use `expect`/`actual`:
```kotlin
// commonMain
expect class EngineProvider() {
    fun getEngine(): HttpClientEngineFactory
}

// androidMain
actual class EngineProvider {
    actual fun getEngine() = Android
}

// iosMain
actual class EngineProvider {
    actual fun getEngine() = Darwin
}
```

## 🔑 Key Files to Know

| File | Purpose |
|------|---------|
| `gradle/libs.versions.toml` | All dependency versions |
| `settings.gradle.kts` | Module declarations |
| `build-logic/convention/` | Gradle convention plugins |
| `config/detekt/detekt.yml` | Code quality rules |
| `composeApp/proguard-rules.pro` | Android ProGuard rules |

## ⚡ Common Tasks

### Add a New Feature Module
```bash
# 1. Create directory
mkdir -p sharedFrontend/ui-myfeature/src/commonMain/kotlin/pl/msiwak/ui/myfeature

# 2. Add to settings.gradle.kts
include(":sharedFrontend:ui-myfeature")

# 3. Create build.gradle.kts
# (See AI_QUICK_REFERENCE.md for template)

# 4. Implement Screen, ViewModel, State, UiAction

# 5. Register in Koin
# Add to ViewModelModule.kt
```

### Add a Dependency
```toml
# 1. Edit gradle/libs.versions.toml
[versions]
my-lib = "1.0.0"

[libraries]
my-lib = { module = "com.example:my-lib", version.ref = "my-lib" }

# 2. Use in build.gradle.kts
commonMain.dependencies {
    implementation(libs.my.lib)
}
```

### Run Code Quality Checks
```bash
./gradlew ktlintCheck detekt
./gradlew ktlintFormat  # Auto-fix
```

## 🚨 Critical Rules (Never Break These!)

❌ **NEVER**:
1. Use platform-specific code in `commonMain`
2. Use `!!` (double-bang) operator
3. Create circular module dependencies
4. Access ViewModel from another ViewModel
5. Make network calls in Composables
6. Put business logic in UI layer
7. Skip error handling
8. Create README files in module directories (use only the root `/README.md`)

✅ **ALWAYS**:
1. Use `expect`/`actual` for platform abstractions
2. Use safe calls (`?.`) or `requireNotNull()`
3. Follow dependency flow: UI → Domain → Data
4. Use Navigator for navigation
5. Make API calls in Repository/ViewModel
6. Use StateFlow for reactive state
7. Handle errors with `Result` or try/catch
8. Update only the root README for project documentation

## 🏗️ Architecture Decision Flow

When implementing a feature, ask:

1. **Where does it belong?**
   - UI logic → ViewModel
   - Business rules → UseCase
   - Data operations → Repository
   - API calls → Network layer

2. **Is it platform-specific?**
   - Yes → Use `expect`/`actual`
   - No → Put in `commonMain`

3. **Does it need state?**
   - UI state → `StateFlow` in ViewModel
   - Temporary state → `remember` in Composable
   - Global state → Koin singleton

4. **Does it need navigation?**
   - Create `NavDestination`
   - Add to navigation graph
   - Use `Navigator.navigate()`

## 🧩 Code Style Quick Rules

```kotlin
// ✅ Good
val name: String = "example"
fun loadData() { ... }
data class UserState(val isLoading: Boolean)

// ❌ Bad
var name = "example"  // Use val
fun LoadData() { ... }  // camelCase, not PascalCase
class userState { ... }  // PascalCase for classes
```

## 🔍 Debugging Checklist

When something doesn't work:

- [ ] Did you add module to `settings.gradle.kts`?
- [ ] Did you register in Koin module?
- [ ] Did you add navigation destination to graph?
- [ ] Is dependency in correct source set (commonMain vs androidMain)?
- [ ] Did you implement `actual` for all platforms?
- [ ] Did you run `./gradlew clean build`?
- [ ] Are ktlint and detekt passing?

## 📊 Tech Stack Summary

| Component | Technology | Version |
|-----------|------------|---------|
| Language | Kotlin | 2.2.21 |
| UI | Compose Multiplatform | 1.9.0 |
| DI | Koin | 4.1.0-Beta1 |
| Networking | Ktor | 3.3.3 |
| Serialization | Kotlinx Serialization | 1.9.0 |
| Async | Coroutines | 1.10.1 |
| Navigation | Compose Navigation | 2.9.0 |
| Database (Server) | Exposed + PostgreSQL | 0.53.0 |
| Firebase | GitLive SDK | 2.4.0 |

## 🎨 UI Patterns

### Screen Structure
```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel = koinInject()) {
    val state by viewModel.viewState.collectAsState()
    MyScreenContent(state, viewModel::onUiAction)
}

@Composable
fun MyScreenContent(
    state: MyState,
    onAction: (MyUiAction) -> Unit
) {
    // Pure UI - no ViewModel dependency
}
```

### State Management
```kotlin
class MyViewModel : ViewModel() {
    private val _state = MutableStateFlow(MyState())
    val state = _state.asStateFlow()
    
    fun onUiAction(action: MyUiAction) {
        when (action) {
            MyUiAction.Load -> load()
        }
    }
    
    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Do work...
            _state.update { it.copy(isLoading = false) }
        }
    }
}
```

## 🧪 Testing Pattern

```kotlin
class MyViewModelTest {
    @Test
    fun `action updates state correctly`() = runTest {
        val viewModel = MyViewModel()
        
        viewModel.onUiAction(MyUiAction.Load)
        
        assertTrue(viewModel.state.value.isLoading)
    }
}
```

## 🔧 Build Configuration

### Android
- Min SDK: 24
- Target SDK: 35
- JVM: 17

### iOS
- Targets: x64, arm64, simulatorArm64
- Framework: Static

### Convention Plugins
```kotlin
// In module's build.gradle.kts
plugins {
    id("pl.msiwak.convention.android.config")  // Android settings
    id("pl.msiwak.convention.target.config")   // KMP targets
}
```

## 💡 Pro Tips

1. **Use templates** from Quick Reference for new features
2. **Check existing code** before implementing similar features
3. **Run ktlint** before committing
4. **Test on all platforms** if touching common code
5. **Use version catalog** for all dependencies
6. **Document platform differences** in comments
7. **Keep ViewModels platform-agnostic**
8. **Prefer composition** over inheritance

## 🆘 Getting Help

- **For patterns**: Check existing UI modules (ui-example, ui-ai-generated)
- **For architecture**: See ARCHITECTURE.md
- **For code style**: Run ktlint and detekt
- **For builds**: Check build-logic/convention plugins
- **For dependencies**: Check libs.versions.toml

## 📝 Workflow Checklist

Before starting work:
- [ ] Read this document
- [ ] Review relevant sections in guidelines
- [ ] Check existing similar features
- [ ] Understand module dependencies

While working:
- [ ] Follow MVI pattern
- [ ] Keep code in correct layer
- [ ] Use existing patterns
- [ ] Write descriptive names
- [ ] Handle errors properly

Before committing:
- [ ] Code builds successfully
- [ ] ktlint and detekt pass
- [ ] Tests added/updated
- [ ] No platform code in commonMain
- [ ] Documentation updated if needed

## 🎓 Learning Path

**Day 1**: Project Structure
- Understand module organization
- Review architecture diagrams
- Explore one feature module completely

**Day 2**: Patterns
- Study MVI implementation
- Review expect/actual usage
- Understand DI setup with Koin

**Day 3**: Implementation
- Create a simple feature
- Add navigation
- Write tests

**Day 4**: Platform-Specific
- Implement expect/actual
- Test on multiple platforms
- Handle platform differences

**Day 5**: Advanced
- Complex state management
- Repository patterns
- Error handling strategies

---

## 🚀 Ready to Start?

1. ✅ Read this document
2. ✅ Review [AI_QUICK_REFERENCE.md](AI_QUICK_REFERENCE.md)
3. ✅ Check [ARCHITECTURE.md](ARCHITECTURE.md) for visual understanding
4. ✅ Explore existing code in `sharedFrontend/ui-example/`
5. ✅ Start coding!

**Remember**: When in doubt, check existing code for patterns. This project has consistent conventions throughout.

---

**Questions to Ask Yourself Before Implementation**:
- Which layer does this belong to?
- Is this platform-specific?
- Does similar functionality already exist?
- Am I following the established patterns?
- Will this work on all platforms?

**Good luck! 🎉**

---

**Last Updated**: February 16, 2026
