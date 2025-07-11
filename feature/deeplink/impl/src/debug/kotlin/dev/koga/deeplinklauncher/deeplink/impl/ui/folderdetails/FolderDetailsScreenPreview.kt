package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink.Companion.previewFavorite
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink.Companion.previewNotFavorite
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun FolderDetailsUIPreview() {
    DLLPreviewTheme {
        FolderDetailsUI(
            uiState = FolderDetailsUiState(
                name = "Folder Name",
                description = "Folder Description",
                deepLinks = persistentListOf(
                    previewFavorite,
                    previewNotFavorite
                ),
            ),
            onAction = {},
            onNavigate = {},
            onShowDeleteConfirmation = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun FolderDetailsUIEmptyPreview() {
    DLLPreviewTheme {
        FolderDetailsUI(
            uiState = FolderDetailsUiState(
                name = "Folder Name",
                description = "Folder Description",
                deepLinks = persistentListOf(),
            ),
            onAction = {},
            onNavigate = {},
            onShowDeleteConfirmation = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun FolderDetailsScreenContentPreview() {
    DLLPreviewTheme {
        FolderDetailsScreenContent(
            uiState = FolderDetailsUiState(
                name = "Folder Name",
                description = "Folder Description",
                deepLinks = persistentListOf(
                    previewFavorite,
                    previewNotFavorite
                ),
            ),
            onAction = {},
            onNavigate = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun FolderDetailsScreenContentEmptyPreview() {
    DLLPreviewTheme {
        FolderDetailsScreenContent(
            uiState = FolderDetailsUiState(
                name = "Folder Name",
                description = "Folder Description",
                deepLinks = persistentListOf(
                    previewFavorite,
                    previewNotFavorite
                ),
            ),
            onAction = {},
            onNavigate = {}
        )
    }
}