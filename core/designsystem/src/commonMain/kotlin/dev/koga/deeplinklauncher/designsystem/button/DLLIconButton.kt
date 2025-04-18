package dev.koga.deeplinklauncher.designsystem.button

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DLLIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    IconButton(
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
        content = content,
    )
}
