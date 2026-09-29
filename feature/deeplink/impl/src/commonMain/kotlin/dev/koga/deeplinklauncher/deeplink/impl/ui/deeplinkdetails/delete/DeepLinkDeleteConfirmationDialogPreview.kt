package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.delete

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewLightDark
@Composable
internal fun DeepLinkDeleteConfirmationDialogPreview() {
    DLLPreviewTheme {
        DeepLinkDeleteConfirmationDialog(
            onDismissRequest = {},
            onDelete = {},
        )
    }
}
