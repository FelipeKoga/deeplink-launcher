package dev.koga.deeplinklauncher.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class StatusColors(
    val errorBackground: Color,
    val errorContent: Color,
    val successBackground: Color,
    val successContent: Color,
)

@Immutable
data class AccentColors(
    val favorite: Color,
)

@Immutable
data class DeepLinkColors(
    val text: TextColors,
    val surface: SurfaceColors,
    val border: BorderColors,
    val button: ButtonColors,
    val status: StatusColors,
    val accent: AccentColors,
)

@Immutable
data class TextColors(
    val primary: Color,
    val secondary: Color,
    val muted: Color,
    val placeholder: Color,
    val inverse: Color,
    val error: Color,
)

@Immutable
data class SurfaceColors(
    val background: Color,
    val card: Color,
    val elevated: Color,
    val muted: Color,
    val primary: Color,
)

@Immutable
data class BorderColors(
    val subtle: Color,
    val default: Color,
    val strong: Color,
)

@Immutable
data class ButtonColors(
    val primaryBackground: Color,
    val primaryContent: Color,
    val secondaryBackground: Color,
    val secondaryContent: Color,
)

val LightDeepLinkColors = DeepLinkColors(
    text = TextColors(
        primary = Color(0xFF18181B),
        secondary = Color(0xFF27272A),
        muted = Color(0xFF71717A),
        placeholder = Color(0xFF71717A).copy(alpha = 0.6f),
        inverse = Color(0xFFFAFAFA),
        error = Color(0xFF7F1D1D),
    ),
    surface = SurfaceColors(
        background = Color(0xFFFFFFFF),
        card = Color(0xFFFFFFFF),
        elevated = Color(0xFFF4F4F5),
        muted = Color(0xFFF4F4F5),
        primary = Color(0xFF18181B),
    ),
    border = BorderColors(
        subtle = Color(0xFFF4F4F5),
        default = Color(0xFFE4E4E7),
        strong = Color(0xFF27272A),
    ),
    button = ButtonColors(
        primaryBackground = Color(0xFF18181B),
        primaryContent = Color(0xFFFAFAFA),
        secondaryBackground = Color(0xFFF4F4F5),
        secondaryContent = Color(0xFF18181B),
    ),
    status = StatusColors(
        errorBackground = Color(0xFFFEF2F2),
        errorContent = Color(0xFF7F1D1D),
        successBackground = Color(0xFFE8F5E9),
        successContent = Color(0xFF2E7D32),
    ),
    accent = AccentColors(
        favorite = Color(0xFFFFB300),
    ),
)

val DarkDeepLinkColors = DeepLinkColors(
    text = TextColors(
        primary = Color(0xFFFAFAFA),
        secondary = Color(0xFFE4E4E7),
        muted = Color(0xFFA1A1AA),
        placeholder = Color(0xFFA1A1AA).copy(alpha = 0.6f),
        inverse = Color(0xFF18181B),
        error = Color(0xFFFECACA),
    ),
    surface = SurfaceColors(
        background = Color(0xFF09090B),
        card = Color(0xFF18181B),
        elevated = Color(0xFF111113),
        muted = Color(0xFF27272A),
        primary = Color(0xFFFAFAFA),
    ),
    border = BorderColors(
        subtle = Color(0xFF27272A),
        default = Color(0xFF3F3F46),
        strong = Color(0xFFE4E4E7),
    ),
    button = ButtonColors(
        primaryBackground = Color(0xFFFAFAFA),
        primaryContent = Color(0xFF18181B),
        secondaryBackground = Color(0xFF27272A),
        secondaryContent = Color(0xFFFAFAFA),
    ),
    status = StatusColors(
        errorBackground = Color(0xFF450A0A),
        errorContent = Color(0xFFFECACA),
        successBackground = Color(0xFF14532D),
        successContent = Color(0xFF86EFAC),
    ),
    accent = AccentColors(
        favorite = Color(0xFFFFB300),
    ),
)