package dev.koga.deeplinklauncher.android.core.designsystem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

val defaultTextFieldColors: TextFieldColors
    @Composable get() = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        disabledContainerColor = MaterialTheme.colorScheme.background,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
    )

@Composable
fun DLLTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    colors: TextFieldColors = defaultTextFieldColors
) {
    TextField(
        value = value,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        },
        colors = colors
    )

}