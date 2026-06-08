package dev.koga.deeplinklauncher.designsystem.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class DeepLinkShapes(
    val card: Shape = RoundedCornerShape(16.dp),
    val cardLarge: Shape = RoundedCornerShape(24.dp),
    val field: Shape = RoundedCornerShape(24.dp),
    val dialog: Shape = RoundedCornerShape(12.dp),
    val chip: Shape = CircleShape,
    val sheet: Shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
    val icon: Shape = RoundedCornerShape(8.dp),
    val tab: Shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
    val small: Shape = RoundedCornerShape(4.dp),
    val medium: Shape = RoundedCornerShape(6.dp),
)

val defaultDeepLinkShapes = DeepLinkShapes()

val LocalDeepLinkShapes = staticCompositionLocalOf { defaultDeepLinkShapes }

val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp),
)
