package dev.koga.deeplinklauncher.designsystem.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DLLOutlinedIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val colors = DeepLinkTheme.colors
    OutlinedIconButton(
        modifier = modifier,
        onClick = onClick,
        colors = IconButtonDefaults.outlinedIconButtonColors(
            contentColor = colors.surface.primary,
        ),
        border = BorderStroke(1.dp, color = colors.border.default),
        content = content,
    )
}
