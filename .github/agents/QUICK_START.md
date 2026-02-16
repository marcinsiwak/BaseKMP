# 🚀 QUICK START - AI Agents

**BaseKMP Kotlin Multiplatform Project**

---

## ⚡ 30-Second Overview

- **Platform**: Kotlin Multiplatform (Android, iOS, Web, Server)
- **UI**: Compose Multiplatform
- **Architecture**: Clean Architecture + MVI
- **DI**: Koin
- **Network**: Ktor

---

## 📖 Documentation Quick Access

| When You Need... | Go To... |
|------------------|----------|
| 🎯 **Getting Started** | [AI_AGENT_ONBOARDING.md](AI_AGENT_ONBOARDING.md) |
| ⚡ **Quick Lookup** | [AI_QUICK_REFERENCE.md](AI_QUICK_REFERENCE.md) |
| 🏗️ **Architecture** | [ARCHITECTURE.md](ARCHITECTURE.md) |
| 🤔 **Make Decision** | [DECISION_TREE.md](DECISION_TREE.md) |
| 📚 **Deep Dive** | [AI_AGENT_GUIDELINES.md](AI_AGENT_GUIDELINES.md) |
| 🔍 **Find Anything** | [DOCUMENTATION_INDEX.md](DOCUMENTATION_INDEX.md) |

---

## 🎯 Top 6 Rules

1. ✅ **commonMain = platform-agnostic only**
2. ✅ **Use expect/actual for platform code**
3. ✅ **UI → Domain → Data → Network** (never reverse)
4. ✅ **StateFlow for state, Navigator for navigation**
5. ✅ **Never use `!!`** - use `?.` or `requireNotNull()`
6. ✅ **Use only root `/README.md`** - no module READMEs

---

## 🛠️ Common Commands

```bash
# Build
./gradlew build

# Android
./gradlew :composeApp:assembleDebug

# Code quality
./gradlew ktlintCheck detekt
./gradlew ktlintFormat  # Fix formatting

# Tests
./gradlew test
```

---

## 📦 Project Structure

```
composeApp/              # Main app
sharedFrontend/          # Shared UI & logic
  ├── ui-*/              # Feature modules
  ├── data/              # Repositories
  ├── domain/            # Use cases
  └── network/           # HTTP client
server/                  # Backend
build-logic/             # Gradle plugins
```

---

## 🎨 Feature Template (Copy-Paste)

### 1. Create Module
```
sharedFrontend/ui-myfeature/
└── src/commonMain/kotlin/pl/msiwak/ui/myfeature/
    ├── MyFeatureScreen.kt
    ├── MyFeatureViewModel.kt
    ├── MyFeatureState.kt
    └── MyFeatureUiAction.kt
```

### 2. State
```kotlin
data class MyFeatureState(
    val isLoading: Boolean = false,
    val data: List<Item> = emptyList()
)
```

### 3. UiAction
```kotlin
sealed interface MyFeatureUiAction {
    object Load : MyFeatureUiAction
    data class ItemClicked(val id: String) : MyFeatureUiAction
}
```

### 4. ViewModel
```kotlin
class MyFeatureViewModel : ViewModel() {
    private val _state = MutableStateFlow(MyFeatureState())
    val state = _state.asStateFlow()
    
    fun onUiAction(action: MyFeatureUiAction) {
        when (action) {
            MyFeatureUiAction.Load -> load()
            is MyFeatureUiAction.ItemClicked -> handleClick(action.id)
        }
    }
}
```

### 5. Screen
```kotlin
@Composable
fun MyFeatureScreen(viewModel: MyFeatureViewModel = koinInject()) {
    val state by viewModel.state.collectAsState()
    MyFeatureContent(state, viewModel::onUiAction)
}

@Composable
fun MyFeatureContent(
    state: MyFeatureState,
    onAction: (MyFeatureUiAction) -> Unit
) {
    Scaffold {
        // Your UI here
    }
}
```

### 6. Register in Koin
```kotlin
// In ViewModelModule.kt
viewModel { MyFeatureViewModel() }
```

---

## 🔍 Quick Decisions

| Question | Answer |
|----------|--------|
| Where put UI? | `sharedFrontend/ui-{feature}/` |
| Where put logic? | ViewModel or UseCase |
| Where fetch data? | Repository in `data/` |
| Platform-specific? | Use expect/actual |
| Need navigation? | Use Navigator class |
| Need state? | StateFlow in ViewModel |

---

## ❌ Don't Do This

- ❌ Platform code in commonMain
- ❌ Business logic in Composables
- ❌ Use `!!` operator
- ❌ ViewModel accessing another ViewModel
- ❌ Network calls from UI
- ❌ Circular dependencies

---

## ✅ Always Do This

- ✅ Use `val` over `var`
- ✅ Handle errors (Result type)
- ✅ Use coroutines for async
- ✅ Test business logic
- ✅ Run ktlint before commit
- ✅ Follow existing patterns

---

## 🆘 Stuck?

1. Check [DECISION_TREE.md](DECISION_TREE.md) for your scenario
2. Look at existing code in `sharedFrontend/ui-example/`
3. Search docs with [DOCUMENTATION_INDEX.md](DOCUMENTATION_INDEX.md)
4. Copy template from [AI_QUICK_REFERENCE.md](AI_QUICK_REFERENCE.md)

---

## 📊 Tech Stack Quick Ref

| Tech | Version |
|------|---------|
| Kotlin | 2.2.21 |
| Compose MP | 1.9.0 |
| Koin | 4.1.0-Beta1 |
| Ktor | 3.3.3 |
| Coroutines | 1.10.1 |

---

## 🎓 Learning Path (30 min)

1. **5 min**: Scan this card
2. **10 min**: Read [AI_AGENT_ONBOARDING.md](AI_AGENT_ONBOARDING.md)
3. **10 min**: Browse [AI_QUICK_REFERENCE.md](AI_QUICK_REFERENCE.md)
4. **5 min**: Explore existing code

---

**🎯 You're ready! Start with [AI_AGENT_ONBOARDING.md](AI_AGENT_ONBOARDING.md)**

---

*Last Updated: February 16, 2026*
