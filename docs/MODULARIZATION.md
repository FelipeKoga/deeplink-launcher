# Modularization Guide

This document defines how the DeepLink Launcher codebase is organized at the Gradle module and package level.

## Module types

| Type | Gradle path pattern | Responsibility |
|------|---------------------|----------------|
| App shell | `:androidApp`, `:desktopApp` | Platform entry points |
| Composition root | `:shared` | Koin wiring, `AppGraph`, `App.kt` |
| Feature API | `:feature:<name>:api` | Minimum public contracts |
| Feature impl | `:feature:<name>:impl` | Full feature implementation |
| Feature UI (optional) | `:feature:<name>:ui-component` | Reusable Compose widgets shared across features |
| Library | `:library:<name>:api\|impl` | External integrations (device-bridge, purchase) |
| Core | `:core:*` | Shared infrastructure (design system, navigation, database, etc.) |

## api vs impl

### api — keep it minimal

The api module exposes only what other modules need at compile time:

- Domain models (`domain.model`)
- Repository interfaces (`domain.repository`)
- Use case interfaces (`domain.usecase`)
- Serializable navigation routes (`ui.navigation`)

The api module must **not** contain:

- Implementations, mappers, or DTOs
- ViewModels, screens, or Compose UI
- DI modules
- Presentation logic

Use `explicitApi()` and mark public API with `public`.

### impl — everything else

- `data/` — repository implementations, mappers, DTOs, datasources
- `domain/` — use case implementations, internal managers
- `ui/` — screens, ViewModels, components, navigation graphs
- `platform/` — platform-specific helpers (e.g. Android utilities)
- `di/` — Koin modules

### ui-component (when needed)

Create a separate ui-component module when Compose widgets are reused by multiple features without pulling in impl:

- Depends only on `feature:api`, `core:designsystem`, and `core:resources`
- Package: `dev.koga.deeplinklauncher.<feature>.uicomponent`

## Package structure

Root package mirrors the Gradle module name.

### Feature API

```
dev.koga.deeplinklauncher.<feature>.api
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
└── ui/
    └── navigation/
```

### Feature impl

```
dev.koga.deeplinklauncher.<feature>.impl
├── data/
│   ├── repository/
│   ├── mapper/
│   └── dto/              (internal)
├── domain/
│   └── usecase/
├── ui/
│   ├── navigation/
│   └── <screen>/
│       ├── <Screen>.kt
│       ├── <Screen>ViewModel.kt
│       ├── state/
│       └── component/
├── platform/
└── di/
```

## Dependency rules

```
┌─────────────────────────────────────────┐
│  feature:<name>:api                     │
│  Public contracts only                  │
└──────────────────┬──────────────────────┘
                   │ consumed by
┌──────────────────▼──────────────────────┐
│  feature:<name>:impl                    │
│  data + domain + ui + di                │
└──────────────────┬──────────────────────┘
                   │ wired by
┌──────────────────▼──────────────────────┐
│  :shared                                │
│  Depends on all feature impl modules    │
└─────────────────────────────────────────┘
```

- Feature `impl` → own `api` + other features' `api` + `core`
- Feature `impl` **never** → another feature's `impl`
- Only `:shared` → all feature `impl` modules
- `api` **never** → `impl`
- `ui-component` → feature `api` + core UI modules only

## Navigation and DI wiring

1. Each feature api exposes serializable route types under `ui.navigation`.
2. Each feature impl provides a `*NavigationGraph` implementing `NavigationGraph` from `core:navigation`.
3. Each feature impl registers Koin bindings in `di/Module.kt`.
4. `:shared` collects all `NavigationGraph` instances into `AppGraph` and loads all Koin modules.

## Visibility conventions

| Type | Visibility |
|------|------------|
| api contracts | `public` |
| Screens, ViewModels, mappers | `internal` |
| NavigationGraph, Koin module | `public` (consumed by `:shared`) |

## Adding a new feature

1. Create `:feature:<name>:api` with `explicitApi()` and only contracts.
2. Create `:feature:<name>:impl` with data/domain/ui/di packages.
3. Register the module in `settings.gradle.kts`.
4. Add Koin module and `NavigationGraph` to `:shared`.
5. Depend on other features through their `api` modules only.

## Current features

| Feature | api | impl | ui-component |
|---------|-----|------|--------------|
| deeplink | models, repositories, use cases, routes | SQLDelight repos, detail screens, platform use cases | DeepLinkCard, FolderCard |
| home | route entry point | home screen, tabs, onboarding | — |
| settings | route entry point | theme, delete data, products, licenses | — |
| data-transfer | import/export use cases, route | import/export screens | — |

## Future improvements

- Enforce ViewModels to depend on use cases instead of repositories directly
- Add Detekt architecture rules to validate module dependency boundaries
- Evaluate automated checks for api surface minimization
