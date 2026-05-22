package dev.koga.deeplinklauncher.designsystem.button

import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DLLIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        contentColor = DeepLinkTheme.colors.text.primary,
        disabledContentColor = DeepLinkTheme.colors.text.muted,
    ),
    content: @Composable () -> Unit,
) {
    IconButton(
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        content = content,
    )
}
