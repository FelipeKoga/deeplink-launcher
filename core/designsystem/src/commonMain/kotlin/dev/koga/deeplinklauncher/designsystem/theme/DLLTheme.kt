package dev.koga.deeplinklauncher.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val lightScheme = lightColorScheme(
    primary = Color(0xFF18181B),
    onPrimary = Color(0xFFFAFAFA),
    secondary = Color(0xFFF4F4F5),
    onSecondary = Color(0xFF18181B),
    tertiary = Color(0xFFE4E4E7),
    onTertiary = Color(0xFF18181B),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF18181B),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF18181B),
    surfaceVariant = Color(0xFFF4F4F5),
    onSurfaceVariant = Color(0xFF71717A),
    surfaceContainer = Color(0xFFFAFAFA),
    surfaceContainerHigh = Color(0xFFF4F4F5),
    surfaceContainerHighest = Color(0xFFE4E4E7),
    tertiaryContainer = Color(0xFFE4E4E7),
    onTertiaryContainer = Color(0xFF18181B),
    outline = Color(0xFFE4E4E7),
    outlineVariant = Color(0xFFF4F4F5),
    error = Color(0xFFEF4444),
    onError = Color(0xFFFAFAFA),
    errorContainer = Color(0xFFFEF2F2),
    onErrorContainer = Color(0xFF7F1D1D)
)

private val darkScheme = darkColorScheme(
    primary = Color(0xFFFAFAFA),
    onPrimary = Color(0xFF18181B),
    secondary = Color(0xFF27272A),
    onSecondary = Color(0xFFFAFAFA),
    tertiary = Color(0xFF3F3F46),
    onTertiary = Color(0xFFFAFAFA),
    background = Color(0xFF09090B),
    onBackground = Color(0xFFFAFAFA),
    surface = Color(0xFF18181B),
    onSurface = Color(0xFFFAFAFA),
    surfaceVariant = Color(0xFF27272A),
    onSurfaceVariant = Color(0xFFA1A1AA),
    surfaceContainer = Color(0xFF111113),
    surfaceContainerHigh = Color(0xFF18181B),
    surfaceContainerHighest = Color(0xFF27272A),
    tertiaryContainer = Color(0xFF27272A),
    onTertiaryContainer = Color(0xFFFAFAFA),
    outline = Color(0xFF3F3F46),
    outlineVariant = Color(0xFF27272A),
    error = Color(0xFFEF4444),
    onError = Color(0xFFFAFAFA),
    errorContainer = Color(0xFF450A0A),
    onErrorContainer = Color(0xFFFECACA)
)

private val LocalDeepLinkColors = staticCompositionLocalOf<DeepLinkColors> {
    error("DeepLinkColors not provided")
}

object DeepLinkTheme {

    val colors: DeepLinkColors
        @Composable
        get() = LocalDeepLinkColors.current
}

@Composable
fun DLLTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit,
) {

    val colors = if (isDarkTheme) {
        DarkDeepLinkColors
    } else {
        LightDeepLinkColors
    }

    CompositionLocalProvider(
        LocalDeepLinkColors provides colors,
    ) {
        MaterialTheme(
            colorScheme = if (isDarkTheme) darkScheme else lightScheme,
            typography = typography,
            shapes = shapes,
            content = content,
        )
    }
}
