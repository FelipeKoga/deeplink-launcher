package dev.koga.deeplinklauncher.designsystem

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DLLAssistChip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val colors = DeepLinkTheme.colors
    AssistChip(
        shape = CircleShape,
        colors = AssistChipDefaults.elevatedAssistChipColors(
            containerColor = Color.Transparent,
            labelColor = colors.surface.primary,
        ),
        border = AssistChipDefaults.assistChipBorder(
            true,
            borderColor = colors.border.default,
        ),
        modifier = modifier,
        onClick = onClick,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        label = label,
    )
}
