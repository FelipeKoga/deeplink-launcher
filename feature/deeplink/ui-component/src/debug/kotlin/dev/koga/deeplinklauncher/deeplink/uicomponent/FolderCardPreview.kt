package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun FolderCardNoDeepLinkPreview() {
    FolderCard(
        folder = Folder(
            id = "1",
            name = "Folder Name",
            description = "Folder Description",
            deepLinkCount = 0
        ),
        onClick = {}
    )
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun FolderCardWithDeepLinkPreview() {
    FolderCard(
        folder = Folder(
            id = "1",
            name = "Folder Name",
            description = "Folder Description",
            deepLinkCount = 1
        ),
        onClick = {}
    )
}