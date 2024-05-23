package dev.koga.deeplinklauncher.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.DLLHorizontalDivider
import dev.koga.deeplinklauncher.DLLModalBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteFolderBottomSheet(
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
) {
    DLLModalBottomSheet(
        onDismiss = onDismissRequest,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        ),
    ) {
        Column {
            Text(
                text = "Delete folder",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.padding(24.dp),
            )

            DLLHorizontalDivider()

            Text(
                text = "Are you sure you want to delete this folder? " +
                    "\nNote: The deeplinks vinculated to this folder will not be deleted",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(24.dp),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
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

                FilledTonalButton(
                    onClick = onDelete,
                    modifier = Modifier.padding(end = 12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                    ),
                ) {
                    Text(
                        text = "Delete",
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
