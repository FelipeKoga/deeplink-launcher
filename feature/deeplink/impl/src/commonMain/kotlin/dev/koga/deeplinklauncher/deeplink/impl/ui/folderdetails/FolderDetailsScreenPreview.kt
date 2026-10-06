package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.state.FolderDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewDeepLinkListItems
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewNow
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.collections.immutable.persistentListOf

@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun FolderDetailsUIEmptyPreview() {
    DLLPreviewTheme {
        FolderDetailsUI(
            uiState = FolderDetailsUiState(
                name = "Work",
                description = "Links used at work",
                deepLinks = persistentListOf(),
                isDeepLinksLoaded = true,
                isFolderLoaded = true,
            ),
            onAction = {},
            onNavigate = {},
            onBack = {},
            onShowDeleteConfirmation = {},
            now = { previewNow },
        )
    }
}

@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun FolderDetailsUIPopulatedPreview() {
    DLLPreviewTheme {
        FolderDetailsUI(
            uiState = FolderDetailsUiState(
                name = "Work",
                description = "Links used at work",
                deepLinks = previewDeepLinkListItems,
                isDeepLinksLoaded = true,
                isFolderLoaded = true,
            ),
            onAction = {},
            onNavigate = {},
            onBack = {},
            onShowDeleteConfirmation = {},
            now = { previewNow },
        )
    }
}
