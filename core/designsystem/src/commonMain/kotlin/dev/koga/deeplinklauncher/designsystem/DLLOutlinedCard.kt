package dev.koga.deeplinklauncher.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DLLOutlinedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = DeepLinkTheme.shapes.card,
    containerColor: Color = Color.Transparent,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = DeepLinkTheme.colors

    if (onClick != null) {
        OutlinedCard(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            border = BorderStroke(1.dp, colors.border.subtle),
            colors = CardDefaults.outlinedCardColors(
                containerColor = containerColor,
                contentColor = colors.text.primary,
            ),
            content = content,
        )
    } else {
        OutlinedCard(
            modifier = modifier,
            shape = shape,
            border = BorderStroke(1.dp, colors.border.default),
            colors = CardDefaults.outlinedCardColors(
                containerColor = containerColor,
                contentColor = colors.text.primary,
            ),
            content = content,
        )
    }
}
