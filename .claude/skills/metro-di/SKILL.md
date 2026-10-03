---
name: metro-di
description: Dependency injection with Metro 1.4.5 in this Compose Multiplatform repo (Android, iOS, desktop), and the incremental migration off Koin. Use when adding or changing an injected class, a binding, a ViewModel, a graph or an app entry point, when moving a Koin module to Metro, when a Metro compile error appears (MissingBinding, DuplicateBinding, scope or visibility errors), or when the user mentions Metro, DI, dependency graph, @Inject, @ContributesBinding, @SingleIn, metroViewModel or Koin.
---

# Metro DI in deeplink-launcher

Metro is a compile-time DI framework: a compiler plugin generates the graph and reports missing or duplicate bindings as build errors. Reference docs: https://zacsweers.github.io/metro/1.4.5/. MetroX ViewModel docs: the `metrox-viewmodel` and `metrox-viewmodel-compose` READMEs in https://github.com/ZacSweers/metro/tree/1.4.5.

The repo is moving from Koin to Metro one layer at a time. Until the migration ends, both containers run side by side (see "Koin bridge"). The end state is `git grep -i koin` returning nothing.

## 1. Toolchain gate

These are already on `main`. Don't downgrade any of them:

- **Gradle daemon on JDK 21.** `gradle/gradle-daemon-jvm.properties` makes Gradle pick or download JDK 21. The Metro compiler and Gradle plugin are Java 21 bytecode. Compiled output stays on `jvmToolchain(17)`.
- **Kotlin 2.4.20.** Metro 1.4.5's iOS klibs carry `abi_version=2.4.0`. Native cross-module aggregation and `generateContributionProviders` both need Kotlin 2.3.20 or newer.
- **Gradle 8.14.3, no `metro {}` block.** The Metro Gradle plugin is compiled for Gradle 9's Kotlin DSL. On 8.14.3 it is a `runtimeOnly` dependency of `build-logic`, applied by id in the `dev.koga.deeplinklauncher.metro` convention plugin. Options go in `gradle.properties` as `metro.<option>=<value>`; every DSL option falls back to a Gradle property of the same name.
- `metro.generateContributionProviders=true` is set. It lets `@Contributes*` classes stay `internal` in their own module.

A module opts in with `alias(libs.plugins.deeplinkLauncher.metro)`. The plugin adds the Metro runtime itself.

## 2. Graph layout

```
shared/commonMain   internal interface AppGraph            (no annotation, the common accessors)
shared/androidMain  @DependencyGraph(AppScope::class) internal interface AndroidAppGraph : AppGraph
                    with a Factory that takes @Provides context: Context
shared/iosMain      @DependencyGraph(AppScope::class) internal interface IosAppGraph : AppGraph
shared/jvmMain      @DependencyGraph(AppScope::class) internal interface DesktopAppGraph : AppGraph
```

Rules:

- **Graphs live in `:shared` only.** `:shared` depends on every impl module, which aggregation needs, and iOS has no other Kotlin module. `androidApp` and `desktopApp` never apply the Metro plugin and never touch a graph type: they only depend on `:shared`, so accessor types from `implementation` deps don't resolve there.
- **Graphs are `internal`.** A public iOS graph exports Metro's merged binding methods into the `shared` Objective-C header.
- **A graph can't extend another graph**, so the common interface has no annotation and each platform declares its own `@DependencyGraph`.
- **`AppInitializer` is the only entry point.** Its platform actuals create the graph with `createGraph<T>()` or `createGraphFactory<T.Factory>().create(...)`. Android passes the `Application` as `Context`. The Swift call `AppInitializer.shared.doInit()` stays unchanged.
- Never put the graph in a `CompositionLocal` and never look bindings up from composables. The Metro maintainer advises against both (ZacSweers/metro discussion #2023). Pass dependencies through constructors; screens get ViewModels from `metroViewModel()`.

There is only one scope, `AppScope`, and no graph extensions. The app never used Koin scopes.

## 3. Koin to Metro mapping

**Every former Koin `single` becomes `@SingleIn(AppScope::class)`, without case-by-case judgment.** A missing scope fails silently:

- the `Channel`s in `AppNavigatorImpl` and `SnackBarDispatcherImpl` would split between consumers;
- a second DataStore instance for the same file throws;
- a second database instance leaves SQLDelight flows stale.

| Koin | Metro |
|---|---|
| `singleOf(::Impl) bind Api::class` | `@SingleIn(AppScope::class) @ContributesBinding(AppScope::class) internal class Impl(...) : Api` |
| `singleOf(::Concrete)` | `@SingleIn(AppScope::class) @Inject class Concrete(...)` |
| `single { Foo(get(), Dispatchers.IO) }`, or anything with default parameters | `@Provides @SingleIn` in a `@BindingContainer`, see below |
| `single<I>` in `androidMain` / `noOpMain` | `@ContributesBinding` on the class in that source set; contributions from platform source sets merge by scope |
| `expect val platformModule` + `includes(...)` | Delete. Platform contributions need no wiring |
| `single<Context> { app }` | `@Provides context: Context` on `AndroidAppGraph.Factory` |
| `getAll<NavigationGraph>()` + `bind NavigationGraph::class` | `@ContributesIntoSet(AppScope::class)` on each graph, injected as `Set<NavigationGraph>` |
| `viewModelOf(::Vm)` | `@ViewModelKey @ContributesIntoMap(AppScope::class) @Inject class Vm(...)`, see §5 |
| `koinViewModel()` | `metroViewModel()` / `assistedMetroViewModel()` |
| `koinInject()` | A constructor parameter |
| `koin.get<T>()` at startup | An accessor on `AppGraph` |

`@Contributes*` implies `@Inject`; adding both is allowed. An `@Inject` class must have exactly one injectable constructor.

Binding container for things that need logic or platform APIs:

```kotlin
@ContributesTo(AppScope::class)
@BindingContainer
object DatabaseBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun database(context: Context): DeepLinkLauncherDatabase =
        DatabaseProvider(AndroidDriverFactory(context)).create()
}
```

Keep provider signatures public and build internal helpers (`DriverFactory`, `DatabaseProvider`, `Adb`, `Xcrun`, the raw `DataStore<Preferences>`) inside the body, so those types never become graph keys.

Repo-specific bindings:

- **`AppDispatchers`** has default parameters, which Metro treats as optional bindings. Provide it with `@Provides fun appDispatchers() = AppDispatchers()`; never `@Inject` it.
- **Device bridge.** `Adb`, `Xcrun` and `CompositeDeviceBridge` are all `DeviceBridge`s. Use one `@Provides @SingleIn fun deviceBridge(): DeviceBridge = CompositeDeviceBridge(Adb.build(io), Xcrun.build(io))`. `@ContributesBinding` on more than one of them is a DuplicateBinding. `DesktopAppGraph` must be able to see the `DeviceBridge` type, so `:shared` jvmMain also needs the device-bridge api module.
- **JVM `DeepLinkTargetStateManagerImpl`** takes `Dispatchers.IO`. Use a jvmMain `@Provides`; don't add a bare `CoroutineDispatcher` binding.
- **`expect class` with no common constructor** (core/file, `ClipboardTextReader`): use per-platform `@Provides`. `@Inject actual class` also works (the spike proved it on JVM and iOS) when every actual has an injectable constructor.

## 4. Visibility

- Contributed classes can stay `internal` thanks to `generateContributionProviders`, but `:shared` must be able to bind every constructor input. A public contributed class that takes an internal type fails to compile.
- Some classes must be public because contributed internal classes take them as inputs across modules. Known ones: `GetDeepLinkFromClipboard`, `ClipboardTextReader` and `AndroidHandlerResolver`.
- Assisted ViewModels (§5) and their factories must be public. Their public members then expose their UI state types, so those become public too.
- Modules with `explicitApi()` need explicit `public` modifiers.

## 5. ViewModels (MetroX)

Dependencies: `dev.zacsweers.metro:metrox-viewmodel-compose`. It needs AndroidX lifecycle 2.10.0, one minor above what CMP 1.10.3 brings.

Graph and factory, both in `:shared`:

```kotlin
internal interface AppGraph : ViewModelGraph

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
internal class AppViewModelFactory(
    override val viewModelProviders: Map<KClass<out ViewModel>, () -> ViewModel>,
    override val assistedFactoryProviders: Map<KClass<out ViewModel>, () -> ViewModelAssistedFactory>,
    override val manualAssistedFactoryProviders:
    Map<KClass<out ManualViewModelAssistedFactory>, () -> ManualViewModelAssistedFactory>,
) : MetroViewModelFactory()
```

The root composable provides it once: `CompositionLocalProvider(LocalMetroViewModelFactory provides graph.metroViewModelFactory)`. `ViewModelGraph` declares its maps with `allowEmpty = true`, so a graph with no ViewModels is still valid.

**Plain ViewModel:**

```kotlin
@ViewModelKey
@ContributesIntoMap(AppScope::class)
@Inject
internal class ExportViewModel(...) : ViewModel(), AppNavigator by appNavigator
```

If the class has more than one supertype (`SettingsViewModel`, `ExportViewModel` and `ImportViewModel` delegate `AppNavigator`), use `@ContributesIntoMap(AppScope::class, binding<ViewModel>())`. The call site is `val viewModel: ExportViewModel = metroViewModel()`.

**ViewModel with `SavedStateHandle`** (`DeepLinkDetails`, `FolderDetails`, `LinkDeepLinkForFolder`, `AddFolder`):

```kotlin
@AssistedInject
class AddFolderViewModel(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val repository: FolderRepository,
) : ViewModel() {
    @AssistedFactory
    @ViewModelAssistedFactoryKey(AddFolderViewModel::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ViewModelAssistedFactory {
        override fun create(extras: CreationExtras): AddFolderViewModel = create(extras.createSavedStateHandle())
        fun create(@Assisted savedStateHandle: SavedStateHandle): AddFolderViewModel
    }
}
```

The call site is `assistedMetroViewModel<AddFolderViewModel>()`. It passes the back-stack entry's extras, so `toRoute()` and `getStateFlow` keep working unchanged.

## 6. Koin bridge (temporary)

Metro takes over bottom-up, so Metro never needs an instance that Koin owns. Koin reads from Metro, never the reverse:

1. The graph is created before `startKoin` on every platform.
2. `AppGraph` extends the `KoinBridge` interface, which has one accessor per type Metro owns that Koin code still needs.
3. `startKoin` loads `module { single { graph.appCoroutineScope } ... }` built from those accessors.

A migration PR moves one layer on every platform at once and deletes its Koin definitions in the same PR, so each type has exactly one owner. Add a bridge accessor for every moved type that some Koin definition or `koinViewModel` still resolves. Remove accessors as the last Koin consumers go. The startup PR deletes the bridge, `startKoin`, the `platformModule` expect/actuals in `:shared`, and the Koin modules.

Koin is only checked at runtime, so a missing bridge accessor crashes when the screen that needs it opens.

## 7. Verification

Compile all three graphs. Metro validates each graph when its platform compiles:

```bash
./gradlew :shared:compileDebugKotlinAndroid :shared:compileKotlinJvm :shared:compileKotlinIosSimulatorArm64
```

Full local check, with the debug keystore env from the perf setup:

```bash
./gradlew compileKotlinJvm compileDebugKotlinAndroid compileKotlinIosSimulatorArm64 compileKotlinIosX64 \
  testDebugUnitTest jvmTest iosSimulatorArm64Test ktlint :desktopApp:createDistributable \
  -PexcludeScreenshotTests -x :androidApp:testDebugUnitTest --continue
```

- **Graph tests** live in `:shared` `jvmTest` and `iosTest`. They create the graph and check that scoped accessors return the same instance twice. They must not touch database or DataStore accessors, which would write to the real home directory.
- **Desktop:** never run against the real home directory. Launch the distributable with `JAVA_TOOL_OPTIONS=-Duser.home=$(mktemp -d)`.
- **iOS:** the iOS CI workflow is disabled, so build and launch locally with the Xcode recipe.
- **Android:** run `gh workflow run maestro.yml --ref <branch>`.
- **No automated coverage:** export/import and the Products screen have no Maestro flow. Check them by hand when a PR touches those bindings.

## 8. Pitfalls

- **`[Metro/MissingBinding]`**: the type has no `@Inject` constructor, contribution or `@Provides`, or the contributing module isn't on `:shared`'s classpath. Also check that the module applies the Metro plugin.
- **`[Metro/DuplicateBinding]`**: two contributions for the same key, typically one in common code and one in a platform source set, or a Koin-era duplicate.
- **Scopes:** `@SingleIn` goes on the class or the `@Provides` function, never on `@Binds`.
- **Providers:** `Provider<T>` and `() -> T` are invoked with `()`; there is no `get()`. `Lazy<T>` uses `.value`.
- **Multibindings:** empty sets and maps are an error unless declared with `@Multibinds(allowEmpty = true)`.
- **Graph annotations:** one per class. A type can't be both a `@DependencyGraph` and a `@GraphExtension`.
- **Default parameters** on an `@Inject` constructor become optional bindings, which silently use the default when no binding exists.
- **`[Metro/UnusedGraphInputs]`**: a graph factory parameter (such as Android's `Context`) or binding container that no binding uses. Its severity is the `metro.unusedGraphInputsSeverity` Gradle property.
- The full diagnostic list is at https://zacsweers.github.io/metro/1.4.5/diagnostics/.
