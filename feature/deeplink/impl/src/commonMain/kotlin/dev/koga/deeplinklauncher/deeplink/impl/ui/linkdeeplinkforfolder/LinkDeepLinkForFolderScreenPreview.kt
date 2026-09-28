package dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder.state.LinkDeepLinkForFolderUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewDeepLinkListItems
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewNow
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun LinkDeepLinkForFolderUIPopulatedPreview() {
    DLLPreviewTheme {
        LinkDeepLinkForFolderUI(
            uiState = LinkDeepLinkForFolderUiState(
                folderName = "Work",
                isFolderLoaded = true,
                linkableDeepLinks = previewDeepLinkListItems,
            ),
            onAction = {},
            onNavigate = {},
            now = { previewNow },
        )
    }
}

@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun LinkDeepLinkForFolderUIEmptyPreview() {
    DLLPreviewTheme {
        LinkDeepLinkForFolderUI(
            uiState = LinkDeepLinkForFolderUiState(
                folderName = "Work",
                isFolderLoaded = true,
            ),
            onAction = {},
            onNavigate = {},
            now = { previewNow },
        )
    }
}
