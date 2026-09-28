# Modularization Guide

This document defines how the DeepLink Launcher codebase is organized at the Gradle module and package level.
The reasoning behind it is in [reviews/2026-09-27-capability-vs-feature.md](reviews/2026-09-27-capability-vs-feature.md).

## Module types

| Type | Gradle path pattern | Responsibility |
|------|---------------------|----------------|
| App shell | `:androidApp`, `:desktopApp` | Platform entry points |
| Composition root | `:shared` | Koin wiring, `AppGraph`, `App.kt` |
| Domain api | `:domain:<name>:api` | Contract of a bounded context shared by several features: models, repositories, ports |
| Domain impl | `:domain:<name>:impl` | Schema, repositories, platform actuals of the ports, DI bindings |
| Domain testing | `:domain:<name>:testing` | Fakes owned by the domain and the contract suite they share with the real implementation |
| Feature api | `:feature:<name>:api` | Navigation entry points (routes) of the feature's screens, nothing else |
| Feature impl | `:feature:<name>:impl` | Screens, ViewModels, navigation graph, screen-only logic |
| Feature ui (optional) | `:feature:<name>:ui` | Compose widgets and list mappers other features embed |
| Library | `:library:<name>:api\|impl` | External integrations (device-bridge, purchase, analytics) |
| Core | `:core:*` | Product-agnostic infrastructure (design system, navigation, SQLite drivers, etc.) |

## Feature or domain?

Ask, for every piece of code:

1. Is it a screen or a flow of screens? → `:feature:<name>:impl`, with its routes in `:feature:<name>:api`.
2. Is it data or a rule used only by one feature's screens? → keep it `internal` in that feature's impl.
3. Is it data or a rule used by two or more features, by something without a screen (widget, extension,
   worker), or does it need one owner for its invariants and schema? → `:domain:<name>`.
4. Is it a widget or mapper of a domain entity embedded by two or more features? → `:feature:<owner>:ui`.
5. Does it talk to an external SDK or process? → `:library:<name>`.
6. Would any product need it? → `:core:<name>`.

A domain module has no screens, no navigation and no Compose. Feature routes never live in a domain module.

## api vs impl

### Domain api: contracts of the bounded context

- Models (`model`), repositories (`repository`), use cases and ports (`usecase`, `manager`)
- Repository writes are commands scoped to one record (see `DeepLinkRepository`), never whole-entity upserts
- No UI models, formatting, routes or vendor types; `explicitApi()`

Interfaces exist where there is a real substitution: platform actuals, fakes used by other modules.
Logic with a single common implementation that only one module uses stays a concrete `internal` class.

### Domain impl

- `data/` — SQLDelight schema (`src/commonMain/sqldelight`), repositories, mappers, database setup
- `usecase/`, `manager/` — implementations of the ports, per platform where needed
- `platform/` — platform helpers (Android intents, clipboard)
- `di/` — Koin module (`deepLinkDomainModule`) plus per-platform bindings

Only `:shared` depends on a domain impl.

### Domain testing

- In-memory fakes with the same observable behavior as the real implementation (`FakeDeepLinkStore`)
- An abstract contract suite (`DeepLinkRepositoryContract`) that both the fake and the SQL implementation extend
- Depended on only from test source sets

### Feature api — routes only

A feature api contains the serializable routes other modules navigate to. Domain types come from the
domain api; presentation types stay in the feature.

### Feature impl — everything else of the screens

- `ui/` — screens, ViewModels, components, navigation graph
- `application/` — screen models and screen-only enrichment
- `di/` — Koin module

## Package structure

Root package mirrors the Gradle module name:

```
dev.koga.deeplinklauncher.domain.<name>.api       model/ repository/ usecase/ manager/
dev.koga.deeplinklauncher.domain.<name>.impl      data/ usecase/ manager/ platform/ di/
dev.koga.deeplinklauncher.domain.<name>.testing
dev.koga.deeplinklauncher.<feature>.api           ui/navigation/
dev.koga.deeplinklauncher.<feature>.impl          ui/ application/ di/
dev.koga.deeplinklauncher.<feature>.ui            model/ formatting/ di/
```

## Dependency rules

```
:shared ──► feature:*:impl ──► feature:*:api, feature:*:ui (other features)
   │              │
   │              └──────────► domain:*:api ◄── feature:*:ui
   │                                ▲
   └──────► domain:*:impl ──────────┘
                 │
                 └──────────► core:* (drivers, date, preferences)
```

- Feature `impl` → own `api`/`ui` + other features' `api`/`ui` + `domain:*:api` + `core` + `library:*:api`
- Feature `impl` **never** → another `impl` (feature or domain)
- Feature `api` → `core:navigation` only
- Feature `ui` → `domain:*:api` + core UI modules only
- Domain `api` → `core` only; never Compose, navigation or `library` types
- Domain `impl` → own `api` + `core` + `library:*:api`
- `testing` modules only from test source sets
- Only `:shared` → `impl` modules

## Navigation and DI wiring

1. Each feature api exposes serializable route types under `ui.navigation`.
2. Each feature impl provides a `*NavigationGraph` implementing `NavigationGraph` from `core:navigation`.
3. Each domain impl, feature impl and feature ui registers Koin bindings in `di/Module.kt`.
4. `:shared` loads all Koin modules and collects all `NavigationGraph` instances into `AppGraph`.

## Visibility conventions

| Type | Visibility |
|------|------------|
| api contracts | `public` |
| Screens, ViewModels, mappers, repository implementations | `internal` |
| NavigationGraph, Koin module | `public` (consumed by `:shared`) |

## Adding a new feature

1. Create `:feature:<name>:api` with `explicitApi()` and only routes.
2. Create `:feature:<name>:impl` with ui/di packages.
3. If the feature needs data that other features also use, use or extend a `:domain:*` module instead of
   putting it in the feature.
4. Register the modules in `settings.gradle.kts`, the Koin module and `NavigationGraph` in `:shared`.

## Current modules

| Module | Contents |
|--------|----------|
| `domain:deeplink:api` | `DeepLink`, `Folder`, repositories, launch/handler/shortcut ports, deeplink use cases, device targets (JVM) |
| `domain:deeplink:impl` | SQLDelight schema (`dll-db`), repositories, Android/iOS/JVM actuals |
| `domain:deeplink:testing` | `FakeDeepLinkStore`, `DeepLinkRepositoryContract` |
| `feature:deeplink:{api,impl,ui}` | deeplink details, folder screens; `DeepLinkCard`, `FolderCard`, list mapping |
| `feature:home:{api,impl}` | home screen, tabs, search, onboarding |
| `feature:settings:{api,impl}` | theme, delete data, products, licenses |
| `feature:data-transfer:{api,impl}` | import/export screens and use cases |

## Future improvements

- Enforce these dependency rules with a build check (see the modularization study, §6.8)
- Consolidate the deeplink ports (handler resolver, parser, shortcuts)
- `abiValidation` on api modules; detekt currently does not analyze KMP source sets
