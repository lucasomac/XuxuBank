# AGENTS.md - XuxuBank Development Guide

## 🏗️ Architecture Overview

**XuxuBank** follows **Clean Architecture** with **MVVM** pattern across three layers:

### Layer Organization
- **Domain Layer** (`domain/`): Core business logic, repository interfaces, use cases, and models
  - `domain/model/`: Domain-specific data classes (User, Client, Debt)
  - `domain/repository/`: Repository interfaces abstracted from persistence
  - `domain/usecase/`: ManageUserUseCase, ManageClientUseCase, ManageDebtUseCase (coordinate between ViewModels and Repositories)
  
- **Data Layer** (`data/`): Database, repositories, and entity-to-domain mapping
  - `data/local/`: Room database entities, DAOs, TypeConverters
  - `data/repository/`: Repository implementations using DAOs
  - `data/local/Mapper.kt`: Extension functions for Entity ↔ Domain model conversion
  
- **UI Layer** (`ui/`): Jetpack Compose UI, ViewModels, and navigation
  - `ui/screen/`: Composable screens (LoginScreen, ManagerHomeScreen, UserHomeScreen)
  - `ui/viewmodel/`: MVVM ViewModels using StateFlow and Koin injection
  - `ui/navigation/`: Type-safe navigation with sealed interface NavRoute
  - `ui/component/`: Reusable UI components (DebtItem, StatusChip, XuxuLogo)
  - `ui/theme/`: Material3 theme with Northeast Brazil color palette

### Data Flow
```
ViewModel → UseCase → RepositoryImpl → DAO → Room Database
                   ↓ Domain Models
           Collects from Repository
           Exposes StateFlow
```

## 🔑 Critical Workflows

### Build & Run
```bash
# Build the project (Gradle wrapper, not requiring Maven/setup)
./gradlew build

# Run on emulator/device
./gradlew installDebug

# Run unit tests
./gradlew test

# Run instrumented tests (Android JUnit)
./gradlew connectedAndroidTest

# Clean build cache
./gradlew clean
```

### Database Migrations
- Room database version in `XuxuDatabase.kt` (currently v2)
- Uses `.fallbackToDestructiveMigration(false)` to prevent destructive migrations
- If schema changes: increment SCHEMA_VERSION and add migration or use `fallbackToDestructiveMigration()`
- Three entities: **UserEntity**, **ClientEntity**, **DebtEntity** with TypeConverters for LocalDate

### Code Generation
- **KSP** (Kotlin Symbol Processing) compiles Room entities, Moshi JSON adapters, and navigation serialization
- Explicit `ksp` dependency in build.gradle.kts for Room compiler and Moshi codegen

## 🎯 Project-Specific Patterns

### Dependency Injection with Koin
Located in `di/AppModule.kt` - all dependencies are centralized:
- **Singletons**: Database, DAOs, Repositories (single instance across app lifecycle)
- **Factories**: Use Cases (new instance per request)
- **ViewModels**: Koin's `viewModel` factory automatically scoped to Compose Navigation

```kotlin
// Access in Composables
val viewModel: UserViewModel = koinViewModel()
// or in MainActivity via Koin
val userViewModel: UserViewModel = koinViewModel()
```

### Navigation Model
`NavRoute` is a sealed interface with @Serializable destinations:
- **Type-safe routing**: Compiler validates all route destinations
- **Parameterized routes**: `ClientDetail(id: Long)`, `ClientForm(id: Long? = null)`, `DebtForm(clientId: Long, debtId: Long? = null)`
- **Navigation via NavBackStack**: `rememberNavBackStack(NavRoute.Splash)` + `backStack.add(route)`
- **Display with NavDisplay**: Composable function receives NavEntry and renders appropriate screen

**Pattern**: Define new screens as objects/data classes in NavRoute, implement Composable rendering in MainActivity's NavDisplay lambda.

### ViewModel Pattern
- All ViewModels inherit from `androidx.lifecycle.ViewModel` (injected via Koin)
- Use **StateFlow** for reactive state management: `private val _currentUser = MutableStateFlow<User?>(null)`
- Launch coroutines in `viewModelScope` for async operations (automatic cleanup on ViewModel destruction)
- Example in `UserViewModel`, `ClientViewModel`, `DebtViewModel`

```kotlin
// ViewModels receive Use Cases via Koin injection
class ClientViewModel(private val manageClientUseCase: ManageClientUseCase) : ViewModel() {
    val clients: StateFlow<List<Client>> = manageClientUseCase.allClients
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
```

### Repository Pattern
- **Repository interfaces** in domain layer define contract
- **Repository implementations** in data layer handle DAO access
- **Use Cases** mediate between ViewModels and Repositories, handling entity-to-domain mapping

```kotlin
// Domain interface
interface ClientRepository {
    suspend fun addClient(client: Client): Long
    fun allClients(): Flow<List<Client>>
}

// Implementation with DAO
class ClientRepositoryImpl(private val clientDao: ClientDao) : ClientRepository {
    override suspend fun addClient(client: Client): Long = 
        clientDao.insertClient(ClientEntity.fromDomain(client))
}
```

### Entity-to-Domain Mapping
Extension functions in `data/local/Mapper.kt` convert between persistence and domain layers:
```kotlin
// Entity → Domain
fun ClientEntity.toDomain() = Client(id, clientName)

// Domain → Entity
fun Client.toEntity() = ClientEntity(id, clientName)
```

### Role-Based Access Control (RBAC)
- **UserRole enum**: MANAGER, CLIENT (stored in UserEntity)
- Navigation logic branches on currentUser role in MainActivity
- Manager sees all clients/debts; Client sees only their own debts
- Use `currentUser.role == UserRole.MANAGER` to gate UI sections

### Jetpack Compose UI Patterns
- **Adaptive layouts**: Uses `androidx.compose.material3.adaptive.ListDetailSceneStrategy` for responsive List-Detail pattern
- **Material3 theme**: `XuxuBankTheme` in `ui/theme/Theme.kt` defines color palette
- **Northeast Brazil colors**: #FDB813 (gold), #C62828 (red), #FF6D00 (orange), #B8860B (dark gold)
- **Localized strings**: `stringResource(R.string.*)` for pt-BR language support
- **State collection**: `collectAsState()` converts StateFlow to Compose state

### Database Schema
Three Room entities with relationships:
- **UserEntity**: id (PK), name, email, role (MANAGER/CLIENT)
- **ClientEntity**: id (PK), clientName
- **DebtEntity**: id (PK), description, amount, clientId (FK), dueDate (nullable), status (PAID/PENDING/OVERDUE)

## 🔗 Integration Points

### External APIs & Libraries
- **Retrofit + Moshi**: HTTP client with JSON serialization (configured but not actively used in current screens - available for future backend integration)
- **Coroutines**: Async/await with `kotlinx.coroutines.core` and `kotlinx.coroutines.android`
- **OkHttp + LoggingInterceptor**: Network debugging and request logging
- **Camera**: Camera2 module for future QR code scanning features (dependency present, not yet implemented)
- **Location Services**: GPS integration with Google Play Services (configured for future features)

### Cross-Component Communication
- **ViewModel to ViewModel**: No direct coupling; communicate through shared state in MainActivity's NavEntry
- **Scale to multi-screen**: Extend NavRoute with new serializable destinations and add Composables to NavDisplay
- **Testing**: ViewModels are testable via StubRepository implementations (unit tests in `test/` directory)

## 📝 Naming Conventions & Terminology

- **Terminology shift**: All references use "Client" not "Debtor"/"Devedor" (business alignment)
- **Status enum**: PAID, PENDING, OVERDUE (mapped to Portuguese UI strings)
- **Method naming**: CRUD operations named `add/get/update/delete` + entity name
- **Use Case suffix**: Classes ending in `UseCase` coordinate multi-repo operations
- **Entity suffix**: Room data classes named `*Entity` (ClientEntity, DebtEntity, UserEntity)
- **ViewModel suffix**: UI ViewModels named `*ViewModel`
- **Composable naming**: Screen functions capitalized, component functions lowercase-start (e.g., `ManagerHomeScreen`, `debtItem`)

## 🚀 Getting Started with Code Changes

### Adding a New Feature
1. **Define domain model** → `domain/model/NewFeature.kt`
2. **Create repository interface** → `domain/repository/NewFeatureRepository.kt`
3. **Implement repository** → `data/repository/NewFeatureRepositoryImpl.kt` (using DAO)
4. **Create use case** → `domain/usecase/ManageNewFeatureUseCase.kt`
5. **Create ViewModel** → `ui/viewmodel/NewFeatureViewModel.kt`
6. **Add Koin injection** → Update `di/AppModule.kt`
7. **Create Composables** → `ui/screen/NewFeatureScreen.kt` or `ui/component/NewFeatureItem.kt`
8. **Add navigation routes** → Extend `ui/navigation/NavRoute.kt` sealed interface
9. **Wire in MainActivity** → Add case to NavDisplay lambda

### Common Commands
```bash
# Rebuild Compose/Room KSP code
./gradlew build --rerun-tasks

# Format code (Kotlin style)
./gradlew formatKotlin

# Debug on device
./gradlew installDebug
adb logcat | grep xuxubank

# Measure build time
./gradlew build --profile
```

## 📚 Key File Reference

| File | Purpose |
|------|---------|
| `di/AppModule.kt` | All dependency injection configuration |
| `ui/navigation/NavRoute.kt` | Type-safe navigation destinations |
| `MainActivity.kt` | App entry point, NavDisplay orchestration |
| `data/local/XuxuDatabase.kt` | Room database schema and DAOs |
| `ui/screen/LoginScreen.kt` | Role selection and authentication (Client/Manager) |
| `ui/screen/ManagerHomeScreen.kt` | Main manager dashboard with clients list |
| `ui/viewmodel/*ViewModel.kt` | State management and business logic |
| `domain/usecase/*.kt` | Repository orchestration, domain logic |
| `data/local/Mapper.kt` | Entity ↔ Domain conversion functions |
| `gradle/libs.versions.toml` | Centralized dependency versions |

## ⚡ Quick Tips

- **StateFlow is cold**: Components only update when actively collecting
- **Use suspend functions**: Wrap database calls in UseCase with `withContext(Dispatchers.IO)`
- **Adaptive layout**: ListDetailSceneStrategy automatically switches between list/detail on large screens
- **Role-based UI**: Branch on `currentUser?.role == UserRole.MANAGER` for different screens
- **Dates are handled locally**: Use `LocalDate` for due dates; no server sync (future enhancement)
- **Composables are recomposable**: Avoid side effects outside LaunchedEffect, remember blocks
- **Test with mocking**: Replace repository with test implementation in unit tests

