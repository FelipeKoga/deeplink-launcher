package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.delete

import androidx.compose.runtime.Composable
import dev.koga.deeplinklauncher.designsystem.dialog.DLLConfirmationDialog

@Composable
internal fun DeepLinkDeleteConfirmationDialog(
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
) {
    DLLConfirmationDialog(
        onDismissRequest = onDismissRequest,
        title = "Delete DeepLink",
        message = "Are you sure you want to delete this deeplink?",
        confirmLabel = "Delete",
        onConfirm = onDelete,
    )
}
