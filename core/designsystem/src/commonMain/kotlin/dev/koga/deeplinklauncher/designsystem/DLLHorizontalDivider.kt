package dev.koga.deeplinklauncher.designsystem

import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DLLHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = DeepLinkTheme.colors.border.subtle,
    )
}
