package dev.koga.deeplinklauncher.designsystem.button

import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DLLFilledIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(
        containerColor = DeepLinkTheme.colors.button.secondaryBackground,
        contentColor = DeepLinkTheme.colors.button.secondaryContent,
    ),
    content: @Composable () -> Unit,
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        content = content,
    )
}
