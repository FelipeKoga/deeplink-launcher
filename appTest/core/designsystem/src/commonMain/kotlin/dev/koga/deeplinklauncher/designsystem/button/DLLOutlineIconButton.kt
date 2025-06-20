package dev.koga.deeplinklauncher.designsystem.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DLLOutlinedIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    OutlinedIconButton(
        modifier = modifier,
        onClick = onClick,
        colors = IconButtonDefaults.outlinedIconButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.surfaceVariant),
        content = content,
    )
}
