package dev.koga.deeplinklauncher.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val primaryTextColor = Color(0xFFE0E0E0)
val secondaryTextColor = Color(0xFFB3B3B3)

val colors = darkColorScheme(
    primary = Color(0xFFE0E0E0), // Soft white for primary elements
    onPrimary = Color(0xFF2C2C2C), // Very dark gray for text/icons on primary, softer than pure black
    onSecondary = Color(0xFFE0E0E0), // Soft white for text/icons on secondary elements
    background = Color(0xFF121212), // Slightly lighter dark gray for background, less harsh than pure black
    surface = Color(0xFF121212), // Matching the background for consistency
    onSurface = Color(0xFFE0E0E0), // Soft white for text/icons on surface
    secondaryContainer = Color(0xFF2C2C2C), // Very dark gray for secondary containers, for slight contrast
    onSecondaryContainer = Color(0xFFE0E0E0), // Soft white for text/icons on secondary containers
)
