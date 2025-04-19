package dev.koga.deeplinklauncher.settings.ui.suggestions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionsOptionBottomSheet(
    viewModel: SuggestionsOptionViewModel,
    onDismissRequest: () -> Unit,
) {
    val enabled = viewModel.enabled

    DLLModalBottomSheet(onDismiss = onDismissRequest) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Suggestions",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "When you are typing a deeplink, suggestions will be shown below the input" +
                        " based on the deeplinks you already launched.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                ),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Enable suggestions",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.weight(1f),
                )

                Spacer(modifier = Modifier.height(24.dp))

                Switch(
                    checked = enabled,
                    onCheckedChange = {
                        viewModel.update(enabled)
                        onDismissRequest()
                    },
                )
            }
        }
    }
}
