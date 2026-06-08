package dev.koga.deeplinklauncher.designsystem

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DLLSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = DeepLinkTheme.colors
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = colors.button.primaryContent,
            checkedTrackColor = colors.button.primaryBackground,
            checkedBorderColor = colors.button.primaryBackground,
            checkedIconColor = colors.button.primaryBackground,
            uncheckedThumbColor = colors.surface.card,
            uncheckedTrackColor = colors.border.default,
            uncheckedBorderColor = colors.border.default,
            uncheckedIconColor = colors.text.muted,
            disabledCheckedThumbColor = colors.button.primaryContent.copy(alpha = 0.38f),
            disabledCheckedTrackColor = colors.button.primaryBackground.copy(alpha = 0.12f),
            disabledUncheckedThumbColor = colors.surface.card.copy(alpha = 0.38f),
            disabledUncheckedTrackColor = colors.border.default.copy(alpha = 0.12f),
        ),
    )
}
