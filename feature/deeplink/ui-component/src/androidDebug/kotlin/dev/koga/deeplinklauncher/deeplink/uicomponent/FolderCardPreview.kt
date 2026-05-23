package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.uicomponent.preview.previewFolder
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun FolderCardNoDeepLinkPreview() {
    FolderCard(
        folder = previewFolder,
        onClick = {},
    )
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun FolderCardWithDeepLinkPreview() {
    FolderCard(
        folder = previewFolder,
        onClick = {},
    )
}
