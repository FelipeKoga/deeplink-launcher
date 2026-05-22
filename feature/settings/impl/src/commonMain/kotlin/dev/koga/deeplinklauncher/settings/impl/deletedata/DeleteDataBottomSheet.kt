package dev.koga.deeplinklauncher.settings.impl.deletedata

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDataBottomSheet(
    viewModel: DeleteDataViewModel,
    onDismissRequest: () -> Unit,
) {
    DLLModalBottomSheet(onDismiss = onDismissRequest) {
        DeleteDataBottomSheetContent { deletionType ->
            viewModel.delete(deletionType)
            onDismissRequest()
        }
    }
}

@Composable
internal fun DeleteDataBottomSheetContent(
    onDelete: (DeletionType) -> Unit,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography

    Column(
        modifier = Modifier.padding(24.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Delete data",
            style = typography.title.sheet,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Choose an option and press to confirm.",
            style = typography.body.default,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "This action cannot be undone",
            style = typography.label.chip,
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.text.error,
            ),
            onClick = { onDelete(DeletionType.DEEP_LINKS) },
        ) {
            Text(text = "Delete deeplinks only")
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.text.error,
            ),
            onClick = { onDelete(DeletionType.FOLDERS) },
        ) {
            Text(text = "Delete folders only")
        }

        Spacer(modifier = Modifier.height(24.dp))

        ElevatedButton(
            onClick = { onDelete(DeletionType.ALL) },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = colors.status.errorBackground,
                contentColor = colors.status.errorContent,
            ),
        ) {
            Text(text = "Delete all")
        }
    }
}
