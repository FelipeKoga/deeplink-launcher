package dev.koga.deeplinklauncher.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val lightScheme = lightColorScheme(
    primary = Color(0xFF232324),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFF4F4F5),
    onSecondary = Color(0xFF232324),
    background = Color(0xFFfbfbfb),
    onBackground = Color(0xFF232324),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF232324),
    surfaceVariant = Color(0xFFF9F9FA),
    onSurfaceVariant = Color(0xFF232324),
)

private val darkScheme = darkColorScheme(
    primary = Color(0xFFFAFAFA),
    onPrimary = Color(0xFF09090B),
    secondary = Color(0xFFA1A1AA),
    onSecondary = Color(0xFF09090B),
    background = Color(0xFF09090B),
    onBackground = Color(0xFFFAFAFA),
    surface = Color(0xFF151518),
    onSurface = Color(0xFFFAFAFA),
    surfaceVariant = Color(0xFF27272A),
    onSurfaceVariant = Color(0xFFFAFAFA),
)

@Composable
fun DLLTheme(
    theme: Theme,
    content: @Composable () -> Unit,
) {
    val colorScheme = when (theme) {
        Theme.LIGHT -> lightScheme
        Theme.DARK -> darkScheme
        Theme.AUTO -> if (isSystemInDarkTheme()) darkScheme else lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content,
    )
}
