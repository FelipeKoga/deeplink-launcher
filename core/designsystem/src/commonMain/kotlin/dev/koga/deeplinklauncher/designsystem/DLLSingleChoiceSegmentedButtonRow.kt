package dev.koga.deeplinklauncher.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DLLSingleChoiceSegmentedButtonRow(
    modifier: Modifier = Modifier,
    options: ImmutableList<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier.horizontalScroll(scrollState).border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            shape = RoundedCornerShape(24.dp),
        ),
        horizontalArrangement = Arrangement.Center,
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption

            OutlinedButton(
                onClick = { onOptionSelected(option) },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    contentColor = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                ),
                border = if (isSelected) {
                    BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                } else {
                    null
                },
                modifier = Modifier.padding(horizontal = 4.dp),
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
