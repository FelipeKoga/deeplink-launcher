package dev.koga.deeplinklauncher.designsystem.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewLightDark
@Composable
private fun DLLConfirmationDialogDestructivePreview() {
    DLLPreviewTheme {
        DLLConfirmationDialog(
            onDismissRequest = {},
            title = "Delete folder",
            message = "Are you sure you want to delete this folder?",
            confirmLabel = "Delete",
            onConfirm = {},
        )
    }
}
