# 🤔 Decision Tree for AI Agents - BaseKMP

**Use this flowchart-style guide to make architectural decisions quickly and correctly.**

---

## 🎯 Where Should My Code Go?

### START: I need to implement...

#### 📱 **UI Component / Screen**
```
Q: Is it a reusable component?
├─ YES → sharedFrontend/common-resources/
│         Create @Composable in components/
│
└─ NO → Is it a feature screen?
    └─ YES → sharedFrontend/ui-{feature-name}/
              Create: Screen.kt, ViewModel.kt, State.kt, UiAction.kt
```

#### 🧠 **Business Logic**
```
Q: Is it reusable business logic?
├─ YES → sharedFrontend/domain/
│         Create: UseCase interface
│         sharedFrontend/domain-impl/
│         Create: UseCase implementation
│
└─ NO → Is it screen-specific logic?
    └─ YES → Put in ViewModel of that feature
              sharedFrontend/ui-{feature}/ViewModel.kt
```

#### 💾 **Data Operations**
```
Q: Does it fetch/store data?
├─ YES → Create Repository
│         Interface: sharedFrontend/domain/repository/
│         Implementation: sharedFrontend/data/repository/
│
└─ NO → Is it a simple transformation?
    └─ YES → Extension function in common-model/
```

#### 🌐 **Network Call**
```
Q: Is it a new API endpoint?
├─ YES → sharedFrontend/network/
│         Create: API client function
│         Use in: Repository (data layer)
│
└─ NO → Is it configuration?
    └─ YES → sharedFrontend/network/
              Modify: KtorClient setup
```

#### 📦 **Data Model**
```
Q: Is it shared across features?
├─ YES → sharedFrontend/common-model/
│         Create: @Serializable data class
│
└─ NO → Is it UI-specific?
    └─ YES → Feature module's State.kt
              Example: ui-example/ExampleState.kt
```

#### 🎨 **Resources (Colors, Strings, Images)**
```
Q: Is it used across multiple features?
├─ YES → sharedFrontend/common-resources/
│         Colors: Colors.kt
│         Theme: Theme.kt
│         Strings: strings/
│
└─ NO → Keep in feature module
    └─ ui-{feature}/resources/
```

#### 🗺️ **Navigation**
```
Q: New navigation destination?
└─ YES → 1. Define: sharedFrontend/navigation/destination/
          2. Create graph: ui-{feature}/graph/
          3. Register: composeApp/AppNavHost.kt
```

---

## 🔧 Is It Platform-Specific?

### START: My code needs platform-specific functionality

```
Q: Does it work the same on all platforms?
├─ YES → Put in commonMain/
│
└─ NO → Use expect/actual pattern
    │
    ├─ 1. Define in commonMain:
    │      expect class MyClass {
    │          fun doSomething()
    │      }
    │
    ├─ 2. Implement in androidMain:
    │      actual class MyClass {
    │          actual fun doSomething() {
    │              // Android implementation
    │          }
    │      }
    │
    └─ 3. Implement in iosMain:
           actual class MyClass {
               actual fun doSomething() {
                   // iOS implementation
               }
           }
```

### Common Platform-Specific Scenarios

| Scenario | Solution |
|----------|----------|
| HTTP Engine | ✅ Already exists: `EngineProvider` |
| Dispatchers | ✅ Already exists: `Dispatchers` object |
| Context (Android) | ✅ Already exists: `AppContext` |
| File I/O | Create new expect/actual |
| Permissions | Create new expect/actual |
| Native APIs | Create new expect/actual |

---

## 🧩 Which Module Should Own This?

### Dependency Direction Rule
```
UI Layer ──→ Domain Layer ──→ Data Layer ──→ Network/Database
   ↓              ↓               ↓
Common Model    Common Model   Common Model
```

**If in doubt**: Can Layer B work without Layer A?
- If YES → Put in Layer A (lower level)
- If NO → Put in Layer B (higher level)

### Module Selection Flowchart

```
Q: What does the code do?
│
├─ Displays UI / Handles user input
│  └─ ui-{feature}/ module
│
├─ Coordinates business logic
│  └─ domain/ or domain-impl/
│
├─ Fetches/stores data
│  └─ data/ module
│
├─ Makes HTTP requests
│  └─ network/ module
│
├─ Defines data structures
│  └─ common-model/
│
├─ Navigation logic
│  └─ navigation/
│
├─ App-wide state (loading, etc.)
│  └─ global-loader-manager/
│
└─ Firebase config
   └─ remote-config/
```

---

## 🎨 How Should I Manage State?

### State Management Decision Tree

```
Q: What kind of state is it?
│
├─ UI State (what user sees)
│  │
│  Q: Does it survive configuration changes?
│  ├─ YES → StateFlow in ViewModel
│  │        private val _state = MutableStateFlow(State())
│  │        val state = _state.asStateFlow()
│  │
│  └─ NO → remember in Composable
│           val value = remember { mutableStateOf("") }
│
├─ Navigation State
│  └─ Use Navigator class
│       navigator.navigate(Destination)
│
├─ Loading State
│  │
│  Q: Global or local?
│  ├─ GLOBAL → global-loader-manager
│  └─ LOCAL → State in ViewModel
│
└─ Form Input State
   │
   Q: Needs validation?
   ├─ YES → ViewModel StateFlow
   └─ NO → remember in Composable
```

---

## 🔄 How Should I Handle Async Operations?

### Async Operation Decision Tree

```
Q: Where does the async operation start?
│
├─ User Action (button click)
│  └─ Flow:
│      1. Screen emits UiAction
│      2. ViewModel receives in onUiAction()
│      3. ViewModel launches coroutine:
│         viewModelScope.launch {
│           // async work
│         }
│
├─ Screen Launch (initialization)
│  └─ Options:
│      A. ViewModel init block:
│         init {
│           viewModelScope.launch { /* work */ }
│         }
│      
│      B. Composable LaunchedEffect:
│         LaunchedEffect(Unit) {
│           viewModel.initialize()
│         }
│
└─ Data Refresh (polling, websocket)
   └─ ViewModel with Flow:
       init {
         repository.dataFlow
           .onEach { data -> updateState(data) }
           .launchIn(viewModelScope)
       }
```

### Error Handling Pattern

```kotlin
// ✅ In ViewModel
viewModelScope.launch {
    _state.update { it.copy(isLoading = true) }
    
    repository.getData()
        .onSuccess { data ->
            _state.update { it.copy(
                isLoading = false,
                data = data
            )}
        }
        .onFailure { error ->
            _state.update { it.copy(
                isLoading = false,
                error = error.message
            )}
        }
}

// ✅ In Repository
override suspend fun getData(): Result<Data> = runCatching {
    api.fetchData()
}
```

---

## 🧪 Do I Need to Write Tests?

### Test Decision Tree

```
Q: What am I implementing?
│
├─ New ViewModel
│  └─ YES, write tests
│      Test file: commonTest/
│      Test: State changes for each UiAction
│
├─ New UseCase
│  └─ YES, write tests
│      Test file: commonTest/
│      Test: Business logic correctness
│
├─ New Repository
│  └─ YES, write tests
│      Test file: commonTest/
│      Test: Data transformations, caching
│
├─ Composable UI
│  └─ OPTIONAL
│      Test: Screenshot tests (if critical)
│
└─ Extension function
   └─ IF COMPLEX, write tests
       Test: Edge cases, null handling
```

---

## 📦 Should I Add a Dependency?

### Dependency Addition Decision Tree

```
Q: Do I need a new library?
│
├─ Check: Does functionality exist in stdlib?
│  └─ YES → Use stdlib
│  └─ NO → Continue
│
├─ Check: Is there a KMP library for this?
│  └─ YES → Prefer KMP library
│  └─ NO → Continue
│
├─ Check: Can I implement it myself simply?
│  └─ YES → Implement it
│  └─ NO → Continue
│
└─ Add dependency:
    1. Search: Is it in libs.versions.toml?
       ├─ YES → Use it: implementation(libs.xxx)
       └─ NO → Add to catalog first
    
    2. Check compatibility:
       - Android SDK version
       - iOS compatibility
       - KMP support
    
    3. Add to correct source set:
       commonMain.dependencies { }  // ← Prefer this
       androidMain.dependencies { } // ← Only if needed
       iosMain.dependencies { }     // ← Only if needed
```

---

## 🔀 How Should I Handle Navigation?

### Navigation Decision Tree

```
Q: What kind of navigation?
│
├─ Navigate to new screen
│  └─ Flow:
│      1. Define destination:
│         @Serializable
│         data class DetailDestination(val id: String)
│      
│      2. Add to graph:
│         fun NavGraphBuilder.detailGraph() {
│           composable<DetailDestination> {
│             DetailScreen()
│           }
│         }
│      
│      3. Navigate:
│         viewModel: navigator.navigate(DetailDestination(id))
│
├─ Go back
│  └─ navigator.navigateUp()
│
├─ Deep linking
│  └─ Define navArguments in destination
│
└─ Bottom navigation
   └─ Use separate NavHost with bottomBar
```

---

## 🎯 Quick Decision Matrix

| Scenario | Location | Pattern |
|----------|----------|---------|
| Display list | `ui-{feature}/Screen.kt` | LazyColumn in Composable |
| Handle click | `ViewModel::onUiAction()` | Sealed interface UiAction |
| Fetch data | `Repository` | suspend fun + Result<T> |
| API call | `network/` | Ktor client |
| Business rule | `domain/` | UseCase interface |
| Store value | `ViewModel` | StateFlow |
| Platform-specific | `commonMain + androidMain + iosMain` | expect/actual |
| Navigate | `ViewModel` | Navigator.navigate() |
| Global state | `Koin singleton` | Module definition |
| Loading indicator | `State.isLoading` | Boolean in State |
| Error message | `State.error` | String? in State |
| Form validation | `ViewModel` | State + validation logic |
| One-time event | `SharedFlow` | ViewModel emits, UI collects |
| Resource string | `common-resources/` | Compose resources |

---

## 🚦 Red Flags - Stop and Reconsider

If you're about to:

- ❌ Import Android/iOS SDK in commonMain → **Use expect/actual**
- ❌ Put business logic in Composable → **Move to ViewModel**
- ❌ Access ViewModel from another ViewModel → **Use UseCase or Repository**
- ❌ Make network call in ViewModel directly → **Create Repository**
- ❌ Use `!!` operator → **Use `?.` or `requireNotNull()`**
- ❌ Create circular module dependency → **Restructure layers**
- ❌ Duplicate code across features → **Extract to common module**
- ❌ Skip error handling → **Add try/catch or Result**

---

## ✅ Green Lights - Good to Go

Proceed confidently if you're:

- ✅ Following existing patterns in similar modules
- ✅ Keeping platform code in platform source sets
- ✅ Using Koin for dependency injection
- ✅ Making ViewModels platform-agnostic
- ✅ Writing tests for business logic
- ✅ Using StateFlow for reactive state
- ✅ Handling errors explicitly
- ✅ Following naming conventions

---

## 🔍 Still Unsure? Check These

1. **Find similar code**: Search for `grep -r "similar pattern" .`
2. **Check existing module**: Look at `ui-example/` for reference
3. **Review architecture**: See `ARCHITECTURE.md`
4. **Read guidelines**: See `AI_AGENT_GUIDELINES.md`
5. **Ask**: Is this the simplest solution that works?

---

**Remember**: When in doubt, follow the pattern used in existing code. Consistency > perfection.

---

**Last Updated**: February 16, 2026
