package dev.koga.deeplinklauncher.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun DLLTheme(
    content: @Composable () -> Unit
) {
    val colors = darkColorScheme(
        primary = Color.White,
//        secondary = Color.Gray,
        onPrimary = Color.Black,
        onSecondary = Color.White,
        background = Color(0xFF323232),
        surface = Color(0xFF28272a),
        secondaryContainer = Color(0xFF1c1b1f),
    )

    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
