package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink.Companion.previewFavorite
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink.Companion.previewNotFavorite
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.DeepLinkDetailsUI
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DeleteFolderBottomSheetPreview(
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
) {
    DeleteFolderBottomSheet(
        onDismissRequest = {},
        onDelete = {}
    )
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DeepLinkDetailsUIFavoritePreview() {
    DLLPreviewTheme {
        DeepLinkDetailsUI(
            uiState = DeepLinkDetailsUiState.Edit(
                deepLink = previewFavorite,
                folders = persistentListOf(
                    Folder(
                        id = "1",
                        name = "Folder 1",
                        description = "Folder 1 description",
                        deepLinkCount = 1
                    )
                ),
            ),
            onAction = {},
            onShowDeleteConfirmation = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DeepLinkDetailsUINotFavoritePreview() {
    DLLPreviewTheme {
        DeepLinkDetailsUI(
            uiState = DeepLinkDetailsUiState.Edit(
                deepLink = previewNotFavorite,
                folders = persistentListOf(
                    Folder(
                        id = "1",
                        name = "Folder 1",
                        description = "Folder 1 description",
                        deepLinkCount = 1
                    )
                ),
            ),
            onAction = {},
            onShowDeleteConfirmation = {}
        )
    }
}