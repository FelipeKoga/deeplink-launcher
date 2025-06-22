package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.delete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.dialog.DLLDialog

@Composable
internal fun DeepLinkDeleteConfirmationDialog(
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
) {
    DLLDialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column {
            Text(
                text = "Delete DeepLink",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.padding(24.dp),
            )

            DLLHorizontalDivider()

            Text(
                text = "Are you sure you want to delete this deeplink?",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(24.dp),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.padding(start = 12.dp),
                ) {
                    Text(text = "Cancel", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(24.dp))

                Button(
                    onClick = onDelete,
                    modifier = Modifier.padding(end = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    ),
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }
}
