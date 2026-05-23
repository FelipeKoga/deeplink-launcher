# DeepLink Launcher Design System

For module structure and package conventions, see [MODULARIZATION.md](MODULARIZATION.md).

Styling lives in `core/designsystem` and is exposed via **`DeepLinkTheme`**. Feature code must **not** use `MaterialTheme.colorScheme` or `MaterialTheme.typography`.

---

## Architecture

```
shared/App.kt → DLLTheme(isDarkTheme)
  ├── LocalDeepLinkColors      → DeepLinkTheme.colors
  ├── LocalDeepLinkTypography  → DeepLinkTheme.typography
  ├── LocalDimensions          → DeepLinkTheme.dimensions
  ├── LocalDeepLinkShapes      → DeepLinkTheme.shapes
  └── MaterialTheme            (internal M3 bridge only)
```

| File | Role |
|------|------|
| `Colors.kt` | Semantic color tokens (light/dark) |
| `Typography.kt` | Semantic text styles by UI role |
| `Font.kt` | Nunito + internal `materialTypography()` |
| `Shape.kt` | Semantic shapes + M3 `Shapes` bridge |
| `Dimensions.kt` | Spacing tokens via `DeepLinkTheme.dimensions` |
| `DLL*.kt` | Shared components wired to `DeepLinkTheme` |

Palette: zinc/neutral. Light bg `#FFFFFF`, dark bg `#09090B`.

---

## Usage Pattern

```kotlin
@Composable
fun MyScreen() {
    val dimensions = DeepLinkTheme.dimensions
    val shapes = DeepLinkTheme.shapes

    Text(
        text = "Hello",
        style = DeepLinkTheme.typography.body.default.copy(
            color = DeepLinkTheme.colors.text.primary
        ),
    )
}
```

**Rules:**
1. Declare `colors`, `typography`, `dimensions`, and `shapes` at composable top when styling UI.
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

### `button` — `primaryBackground`/`primaryContent`, `secondaryBackground`/`secondaryContent`, `textContent`/`textDestructiveContent`, `destructiveBackground`/`destructiveContent`

### `chip` — `background`/`content`/`border`, `destructiveContent`/`destructiveBorder`, `accentContent`

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
| Delete flow | `button.destructiveBackground` + `button.destructiveContent` |
| Destructive text action | `button.textDestructiveContent` |
| Destructive chip | `chip.destructiveContent` + `chip.destructiveBorder` |

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

## Spacing — `DeepLinkTheme.dimensions`

Source: `core/designsystem/.../theme/Dimensions.kt`. Provided by `DLLTheme`.

| Token | Value |
|-------|-------|
| `small` | 4 dp |
| `medium` | 8 dp |
| `mediumLarge` | 12 dp |
| `large` | 16 dp |
| `extraLarge` | 24 dp |

Use these instead of magic numbers for padding and spacing.

---

## Shapes — `DeepLinkTheme.shapes`

Source: `core/designsystem/.../theme/Shape.kt`

| Token | Use |
|-------|-----|
| `card` | Standard outlined cards (16 dp) |
| `cardLarge` | Larger cards, e.g. folder grid (24 dp) |
| `field` | Text fields, segmented controls (24 dp) |
| `dialog` | Dialogs, code blocks, detail panels (12 dp) |
| `chip` | Pill/circular chips |
| `sheet` | Bottom bar / sheet top corners (12 dp) |
| `tab` | Tab row top corners (8 dp) |
| `icon` | Icon containers (8 dp) |
| `small` | Compact controls, dropdowns (4 dp) |
| `medium` | Badges, small surfaces (6 dp) |

---

## Components (`core/designsystem`)

| Component | Notes |
|-----------|-------|
| `DLLTopBar` | Top bar; uses `title.topBar` |
| `DLLTextField` / `DLLTextFieldDefaults` | Labeled field; uses `label.field`, `shapes.field` |
| `DLLModalBottomSheet` | Sheet; default `surface.card` |
| `DLLDialog` | Dialog wrapper; `surface.card`, `shapes.dialog` |
| `DLLConfirmationDialog` | Title + divider + message + cancel/confirm actions |
| `DLLHorizontalDivider` | Divider; `border.subtle` |
| `DLLSmallChip` / `DLLAssistChip` | Chips |
| `DLLSingleChoiceSegmentedButtonRow` | Segmented control |
| `DLLButton` | Primary / Secondary / Destructive variants (theme-driven) |
| `DLLTextButton` | Default / Destructive variants (theme-driven) |
| `DLLOutlinedChip` | Outlined action chip; Default / Destructive / Accent variants |
| `DLLOutlinedCard` | Outlined card with theme border/shape |
| `DLLSearchBar` | Docked search bar |
| `DLLSnackbarHost` | Themed snackbar host |
| `DLLListItem` | Settings-style title + description + trailing |
| `DLLCodeBlock` | Monospace JSON/code viewer |
| `DLLIconButton` / `DLLFilledIconButton` / `DLLOutlinedIconButton` | Icon buttons |

### Button variants

| Variant | Use |
|---------|-----|
| `Primary` | Main CTA (Save, Export, Launch) |
| `Secondary` | Secondary filled actions |
| `Destructive` | Delete confirmations |

Text button variants: `Default` | `Destructive`

Outlined chip variants: `Default` | `Destructive` | `Accent` (e.g. favorite)

```kotlin
DLLButton(onClick = { }, text = "Save")
DLLButton(onClick = { }, text = "Delete", variant = DLLButtonVariant.Destructive)
DLLTextButton(onClick = { }, text = "Cancel")
DLLTextButton(onClick = { }, text = "Delete deeplinks only", variant = DLLTextButtonVariant.Destructive)
DLLOutlinedChip(label = "Delete", icon = TrashIcon, variant = DLLOutlinedChipVariant.Destructive, onClick = { })
```

New UI reused in 2+ features → add here, wired to `DeepLinkTheme`.

---

## M3 Bridge (theme layer only)

`DLLTheme.kt` syncs `lightScheme`/`darkScheme` with semantic colors. `Typography.kt` `toMaterialTypography()` maps semantic styles to M3 scale for internal widgets (`TextField`, `TopAppBar`, native chips).

**Do not read `MaterialTheme.colorScheme` or `MaterialTheme.typography` in features or `DLL*` components.**

---

## Extending Tokens

**Color:** add to group in `Colors.kt` → set light/dark values → update M3 bridge in `DLLTheme.kt` if needed → use in UI.

**Typography:** add to group in `Typography.kt` → derive from `materialTypography()` → update `toMaterialTypography()` if needed → use in UI.

**Shape / spacing:** add to `DeepLinkShapes` or `Dimensions` → provide via `DLLTheme` → use in components.

**Component:** create in `core/designsystem/`, use `DeepLinkTheme` internally, expose params not M3 reads.

---

## Anti-patterns

| Avoid | Use |
|-------|-----|
| `MaterialTheme.colorScheme.*` | `DeepLinkTheme.colors.*` |
| `MaterialTheme.typography.*` | `DeepLinkTheme.typography.*` |
| `Color(0xFF...)` in features | Semantic color token |
| Hardcoded `TextStyle(...)` | Semantic typography token |
| Hardcoded `RoundedCornerShape(...)` | `DeepLinkTheme.shapes.*` |
| Hardcoded padding dp values | `DeepLinkTheme.dimensions.*` |
| Raw `Button` / `TextButton` in features | `DLLButton` / `DLLTextButton` |
| `import ...theme.typography` from Font.kt | `DeepLinkTheme.typography` |

---

## File Map

```
core/designsystem/.../designsystem/
├── theme/Colors.kt, Typography.kt, Font.kt, DLLTheme.kt, DLLPreviewTheme.kt, Shape.kt, Dimensions.kt
├── DLLTopBar.kt, DLLTextField.kt, DLLModalBottomSheet.kt, DLLSmallChip.kt, DLLAssistChip.kt
├── DLLOutlinedCard.kt, DLLOutlinedChip.kt, DLLSearchBar.kt, DLLSnackbar.kt, DLLListItem.kt, DLLCodeBlock.kt
├── DLLSingleChoiceSegmentedButtonRow.kt, DLLHorizontalDivider.kt
├── button/DLLButton.kt, DLLTextButton.kt, DLLIconButton.kt, DLLFilledIconButton.kt, DLLOutlineIconButton.kt
├── dialog/DLLDialog.kt, DLLConfirmationDialog.kt
└── utils/LazyStaggeredGridScope.kt
```

App entry: `shared/.../App.kt` → `DLLTheme { ... }`

---

## Agent Checklist

- [ ] Uses `DeepLinkTheme.colors` / `.typography` / `.dimensions` / `.shapes`
- [ ] No `MaterialTheme.colorScheme` or `MaterialTheme.typography` outside theme layer
- [ ] No raw hex in feature modules
- [ ] Text color via `.copy(color = colors.*)`
- [ ] Previews use `DLLPreviewTheme`
- [ ] Light/dark surface contrast verified
- [ ] Prefer `DLLButton` / `DLLTextButton` over raw M3 buttons
