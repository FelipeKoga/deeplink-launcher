# Analytics

DeepLink Launcher uses Firebase Analytics (GA4) on Android to understand product funnels and user behavior. iOS and Desktop use a no-op implementation.

Analytics is **product-oriented** — it tracks funnels, activation, retention, and engagement. It is not used for technical logging (use Crashlytics or local logs for that).

## Architecture

- `library:analytics:api` — generic `AnalyticsTracker` interface with a single `logEvent(name, parameters)` method
- `library:analytics:impl` — `FirebaseAnalyticsTracker` (Android), `NoOpAnalyticsTracker` (iOS/JVM)
- **Product events** live in feature modules (`feature/*/impl/analytics/`) and `shared/analytics/` for app-level events (`app_open`, `screen_view`)
- `LaunchSource` enum lives in `feature:deeplink:api` and is reused by home/deeplink events
- Events are fired from **ViewModels** after business outcomes, not from Compose recompositions
- **Screen views** are centralized in `shared/App.kt` via type-safe route resolution (`AppRoute.analyticsScreenName` + `resolveAnalyticsScreenName()`)
- `app_open` is tracked once in `AppInitializer` after Koin startup

There are **no custom user properties** — cohort segmentation relies on GA4 events and built-in dimensions (platform, app version, etc.).

## Privacy

- Deeplink URLs, query parameters, and tokens are **never** logged
- Events describe user actions (launch, create, delete, navigate), not link content
- No PII is sent

## Events by funnel

### Acquisition

| Event | Parameters |
|-------|------------|
| `app_open` | — |
| `external_link_opened` | `destination` (`play_store`, `github`) |

GA4 also emits `first_open` and `session_start` automatically.

### Activation

| Event | Parameters |
|-------|------------|
| `onboarding_completed` | — |
| `deeplink_launched` | `source` |
| `deeplink_launch_failed` | `source` |
| `deeplink_created` | `source` |
| `folder_created` | — |

**Recommended GA4 key event:** `deeplink_created` with `source=input_bar`, or `deeplink_launched` with `source=input_bar`

### Retention

| Event | Parameters |
|-------|------------|
| `deeplink_launched` | `source` |
| `home_tab_selected` | `tab` (`history`, `favorites`, `folders`) |
| `search_used` | — |
| `data_imported` | `deeplink_count`, `folder_count` |
| `data_exported` | — |

### Engagement

| Event | Parameters |
|-------|------------|
| `screen_view` | `screen_name` |
| `deeplink_details_opened` | `entry_point` |
| `favorite_toggled` | `is_favorite` |
| `deeplink_shared` | — |
| `deeplink_pinned` | `result` (`requested`, `not_supported`) |
| `deeplink_link_copied` | — |
| `deeplink_deleted` | — |
| `deeplink_duplicated` | `copy_all_fields` |
| `folder_link_completed` | — |
| `folder_deleted` | — |
| `data_deleted` | `deletion_type` (`all`, `deep_links`, `folders`) |
| `theme_changed` | `theme` (`light`, `dark`, `auto`) |
| `suggestions_toggled` | `enabled` |
| `purchase_started` | `product_id` |
| `purchase_completed` | `product_id` |
| `purchase_failed` | `product_id`, `user_cancelled` |
| `launch_target_selected` | `target_type` (`desktop`, `device`) — JVM only |

## Screen names

Screen names are defined on each `AppRoute` via `analyticsScreenName` and resolved in `shared/analytics/AnalyticsScreenTracker.kt`.

| Route | `screen_name` |
|-------|---------------|
| Home | `home` |
| Onboarding | `onboarding` |
| DeepLink details | `deeplink_details` |
| Folder details | `folder_details` |
| Add folder | `add_folder` |
| Pick deeplink for folder | `pick_deeplink_for_folder` |
| Settings | `settings` |
| Export | `export_data` |
| Import | `import_data` |
| Theme sheet | `app_theme` |
| Suggestions sheet | `suggestions_option` |
| Delete data sheet | `delete_data` |
| Products sheet | `products` |
| Licenses | `open_source_licenses` |

## Launch sources

Used by `deeplink_launched`, `deeplink_launch_failed`, and `deeplink_created`.

| Value | Meaning |
|-------|---------|
| `input_bar` | Bottom bar on home |
| `list` | Card launch on home |
| `details` | Details bottom sheet |
| `folder` | Folder details screen |
| `link_flow` | Link deeplink to folder flow |

## GA4 funnel setup (Firebase Console)

Recommended exploration funnel:

1. `first_open` (automatic)
2. `onboarding_completed`
3. `deeplink_created` (`source=input_bar`) or `deeplink_launched` (`source=input_bar`)
4. `deeplink_launched` (returning users, day 7)

Mark `deeplink_created` or `deeplink_launched` from `input_bar` as a **key event** to measure activation rate.

## DebugView (Android)

```bash
adb shell setprop debug.firebase.analytics.app dev.koga.deeplinklauncher.android
```

Then open Firebase Console → Analytics → DebugView while using a debug build.
