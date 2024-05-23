package dev.koga.deeplinklauncher

import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun DLLHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
) {
    Divider(
        thickness = thickness,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier,
    )
}
