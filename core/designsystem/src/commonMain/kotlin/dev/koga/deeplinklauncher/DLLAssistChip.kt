package dev.koga.deeplinklauncher

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DLLAssistChip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    AssistChip(
        shape = CircleShape,
        colors = AssistChipDefaults.elevatedAssistChipColors(
            containerColor = Color.Transparent,
            labelColor = MaterialTheme.colorScheme.primary,
        ),
        border = AssistChipDefaults.assistChipBorder(
            true,
            borderColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = modifier,
        onClick = onClick,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        label = label,
    )
}
