package dev.koga.deeplinklauncher.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val lightScheme = lightColorScheme(
    primary = Color(0xFF18181B),
    onPrimary = Color(0xFFFAFAFA),
    secondary = Color(0xFF71717A),
    onSecondary = Color(0xFFFAFAFA),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF18181B),
    surface = Color(0xFFF4F4F5),
    onSurface = Color(0xFF18181B),
    surfaceVariant = Color(0xFFE4E4E7),
    onSurfaceVariant = Color(0xFF18181B),
    tertiaryContainer = Color(0xFFe3e3e3),
    surfaceContainerHighest = Color(0xFFe3e3e3),
    errorContainer = Color(0xFFF44336),
    onErrorContainer = Color(0xFFFAFAFA),
)

private val darkScheme = darkColorScheme(
    primary = Color(0xFFFAFAFA),
    onPrimary = Color(0xFF09090B),
    secondary = Color(0xFFA1A1AA),
    onSecondary = Color(0xFF09090B),
    background = Color(0xFF09090B),
    onBackground = Color(0xFFFAFAFA),
    surface = Color(0xFF0d0d0d),
    onSurface = Color(0xFFFAFAFA),
    surfaceVariant = Color(0xFF27272A),
    onSurfaceVariant = Color(0xFFFAFAFA),
    surfaceContainerHighest = Color(0xFF171717),
    errorContainer = Color(0xFFF44336),
    onErrorContainer = Color(0xFFFAFAFA),
)

@Composable
fun DLLTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (isDarkTheme) darkScheme else lightScheme,
        typography = typography,
        shapes = shapes,
        content = content,
    )
}
