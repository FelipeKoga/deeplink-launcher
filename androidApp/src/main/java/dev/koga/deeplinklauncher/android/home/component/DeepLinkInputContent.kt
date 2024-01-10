package dev.koga.deeplinklauncher.android.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DeepLinkInputContent(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    launch: () -> Unit,
    errorMessage: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
    ) {
        TextField(
            value = value,
            modifier = Modifier
                .defaultMinSize(minHeight = 80.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            onValueChange = onValueChange,
            placeholder = {
                Text(text = "Insert the deeplink here...")
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )

        AnimatedVisibility(
            visible = errorMessage != null,
        ) {
            Text(text = errorMessage.orEmpty(),
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                ))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = launch,
            enabled = value.isNotBlank(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(.6f)
        ) {
            Text(text = "Launch")
        }



    }
}