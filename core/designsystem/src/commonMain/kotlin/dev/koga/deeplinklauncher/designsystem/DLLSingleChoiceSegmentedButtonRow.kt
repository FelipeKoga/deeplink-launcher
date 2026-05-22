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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DLLSingleChoiceSegmentedButtonRow(
    modifier: Modifier = Modifier,
    options: ImmutableList<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier.horizontalScroll(scrollState).border(
            width = 1.dp,
            color = colors.border.default,
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
                        colors.surface.primary
                    } else {
                        colors.surface.card
                    },
                    contentColor = if (isSelected) {
                        colors.text.inverse
                    } else {
                        colors.text.primary
                    },
                ),
                border = if (isSelected) {
                    BorderStroke(1.dp, colors.surface.primary)
                } else {
                    null
                },
                modifier = Modifier.padding(horizontal = 4.dp),
            ) {
                Text(
                    text = option,
                    style = typography.body.default,
                )
            }
        }
    }
}
