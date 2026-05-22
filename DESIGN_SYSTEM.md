# DeepLink Launcher Design System

Styling lives in `core/designsystem` and is exposed via **`DeepLinkTheme`**. Feature code must **not** use `MaterialTheme.colorScheme` or `MaterialTheme.typography`.

---

## Architecture

```
shared/App.kt → DLLTheme(isDarkTheme)
  ├── LocalDeepLinkColors      → DeepLinkTheme.colors
  ├── LocalDeepLinkTypography  → DeepLinkTheme.typography
  └── MaterialTheme            (internal M3 bridge only)
```

| File | Role |
|------|------|
| `Colors.kt` | Semantic color tokens (light/dark) |
| `Typography.kt` | Semantic text styles by UI role |
| `Font.kt` | Nunito + internal `materialTypography()` |
| `Shape.kt` | M3 shapes (via `MaterialTheme.shapes`) |
| `Dimensions.kt` | `LocalDimensions` spacing (limited use) |
| `DLL*.kt` | Shared components wired to `DeepLinkTheme` |

Palette: zinc/neutral. Light bg `#FFFFFF`, dark bg `#09090B`.

---

## Usage Pattern

```kotlin
@Composable
fun MyScreen() {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography

    Text(
        text = "Hello",
        style = typography.body.default.copy(color = colors.text.primary),
    )
}
```

**Rules:**
1. Declare `colors` and `typography` at composable top when styling UI.
2. Apply color via `.copy(color = colors.*)` — typography tokens have no embedded color.
3. Use semantic tokens, not raw `Color(...)`, `TextStyle(...)`, or M3 theme accessors.
4. Prefer `DLL*` components before raw M3.
5. Previews: wrap in `DLLPreviewTheme`.

---

## Colors — `DeepLinkTheme.colors`

Source: `core/designsystem/.../theme/Colors.kt`

### `text`
| Token | Use |
|-------|-----|
| `primary` | Main text, default icons |
| `secondary` | Secondary emphasis, field labels |
| `muted` | Metadata, hints, secondary icons |
| `placeholder` | De-emphasized text (~60% muted) |
| `inverse` | Text on inverted/primary surfaces |
| `error` | Destructive / inline error text |

### `surface`
| Token | Use |
|-------|-----|
| `background` | Screen/scaffold background |
| `card` | Cards, modal bottom sheets |
| `elevated` | Lifted areas (e.g. home bottom bar) |
| `muted` | Soft containers (search, chips) |
| `primary` | Accent tint (icons, links, indicators) |

### `border` — `subtle` | `default` | `strong`

### `button` — `primaryBackground`/`primaryContent`, `secondaryBackground`/`secondaryContent`

### `status` — `errorBackground`/`errorContent`, `successBackground`/`successContent`

### `accent` — `favorite` (`#FFB300`)

### Color decision guide

| Element | Token |
|---------|-------|
| Screen bg | `surface.background` |
| Bottom bar strip | `surface.elevated` |
| Card / sheet | `surface.card` |
| Chip / search container | `surface.muted` |
| Card border | `border.default` |
| Primary CTA fill | `button.primaryBackground` |
| Accent icon / link | `surface.primary` |
| Delete flow | `status.errorBackground` + `status.errorContent` |

**Light mode:** `surface.background` == `surface.card` (both white). Use `surface.elevated` or borders for separation.

---

## Typography — `DeepLinkTheme.typography`

Source: `core/designsystem/.../theme/Typography.kt`. Font: **Nunito**. Sizes follow M3 defaults unless noted.

### `title` — `sheet` | `dialog` | `topBar` | `card` | `page`

### `body` — `default` | `small` | `smallEmphasis` | `emphasis`

### `label` — `section` | `fieldHeader` | `field` | `chip` | `caption` | `badge` | `error`

### `action` — `button` | `tab` | `dropdown` (16sp SemiBold)

### `code` — `link` (monospace URL) | `block` (14sp monospace JSON)

```kotlin
Text(text = "Notes", style = typography.label.fieldHeader.copy(color = colors.text.muted))
Text(text = url, style = typography.code.link.copy(color = colors.surface.primary))
```

Avoid `.copy(fontWeight = ...)` when a token already defines weight. For `AnnotatedString`, use `typography.body.emphasis.toSpanStyle()` or local `FontWeight.Bold` on tutorial copy only.

---

## Components (`core/designsystem`)

| Component | Notes |
|-----------|-------|
| `DLLTopBar` | Top bar; uses `title.topBar` |
| `DLLTextField` | Labeled field; uses `label.field` |
| `DLLModalBottomSheet` | Sheet; default `surface.card` |
| `DLLDialog` | Dialog wrapper |
| `DLLHorizontalDivider` | Divider |
| `DLLSmallChip` / `DLLAssistChip` | Chips |
| `DLLSingleChoiceSegmentedButtonRow` | Segmented control |
| `DLLIconButton` / `DLLFilledIconButton` / `DLLOutlineIconButton` | Icon buttons |

New UI reused in 2+ features → add here, wired to `DeepLinkTheme`.

---

## Spacing — `LocalDimensions`

`small=4`, `medium=8`, `mediumLarge=12`, `large=16`, `extraLarge=24` dp. Limited adoption. Match nearby padding conventions (4/8/12/16/24) before adding magic numbers.

---

## M3 Bridge (theme layer only)

`DLLTheme.kt` syncs `lightScheme`/`darkScheme` with semantic colors. `Typography.kt` `toMaterialTypography()` maps semantic styles to M3 scale for internal widgets (`TextField`, `TopAppBar`, native chips).

**Do not read `MaterialTheme.colorScheme` or `MaterialTheme.typography` in features or `DLL*` components.**

---

## Extending Tokens

**Color:** add to group in `Colors.kt` → set light/dark values → update M3 bridge in `DLLTheme.kt` if needed → use in UI.

**Typography:** add to group in `Typography.kt` → derive from `materialTypography()` → update `toMaterialTypography()` if needed → use in UI.

**Component:** create in `core/designsystem/`, use `DeepLinkTheme` internally, expose params not M3 reads.

---

## Anti-patterns

| Avoid | Use |
|-------|-----|
| `MaterialTheme.colorScheme.*` | `DeepLinkTheme.colors.*` |
| `MaterialTheme.typography.*` | `DeepLinkTheme.typography.*` |
| `Color(0xFF...)` in features | Semantic color token |
| Hardcoded `TextStyle(...)` | Semantic typography token |
| `import ...theme.typography` from Font.kt | `DeepLinkTheme.typography` |

---

## File Map

```
core/designsystem/.../designsystem/
├── theme/Colors.kt, Typography.kt, Font.kt, DLLTheme.kt, DLLPreviewTheme.kt, Shape.kt, Dimensions.kt
├── DLLTopBar.kt, DLLTextField.kt, DLLModalBottomSheet.kt, DLLSmallChip.kt, DLLAssistChip.kt
├── DLLSingleChoiceSegmentedButtonRow.kt, DLLHorizontalDivider.kt
├── dialog/DLLDialog.kt
└── button/DLLIconButton.kt, DLLFilledIconButton.kt, DLLOutlineIconButton.kt
```

App entry: `shared/.../App.kt` → `DLLTheme { ... }`

---

## Agent Checklist

- [ ] Uses `DeepLinkTheme.colors` / `DeepLinkTheme.typography`
- [ ] No `MaterialTheme.colorScheme` or `MaterialTheme.typography` outside theme layer
- [ ] No raw hex in feature modules
- [ ] Text color via `.copy(color = colors.*)`
- [ ] Previews use `DLLPreviewTheme`
- [ ] Light/dark surface contrast verified
