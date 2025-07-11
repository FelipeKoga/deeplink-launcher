package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.delete

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DeepLinkDeleteConfirmationDialogPreview() {
    DeepLinkDeleteConfirmationDialog(
        onDismissRequest = {},
        onDelete = {}
    )
}