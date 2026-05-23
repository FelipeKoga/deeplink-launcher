package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.component

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.DeepLinkDetailsUI
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewFavoriteDeepLink
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewFolder
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewFolderOneDeepLinkCount
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewNotFavoriteDeepLink
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
        onDelete = {},
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
                deepLink = previewFavoriteDeepLink,
                folders = persistentListOf(
                    previewFolder,
                    previewFolderOneDeepLinkCount,
                ),
            ),
            onAction = {},
            onShowDeleteConfirmation = {},
            scrollState = rememberScrollState(),
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
                deepLink = previewNotFavoriteDeepLink,
                folders = persistentListOf(
                    previewFolder,
                    previewFolderOneDeepLinkCount,
                ),
            ),
            onAction = {},
            onShowDeleteConfirmation = {},
            scrollState = rememberScrollState(),
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DeepLinkDetailsUIEmptyPreview() {
    DLLPreviewTheme {
        DeepLinkDetailsUI(
            uiState = DeepLinkDetailsUiState.Edit(
                deepLink = previewFavoriteDeepLink,
                folders = persistentListOf(),
            ),
            onAction = {},
            onShowDeleteConfirmation = {},
            scrollState = rememberScrollState(),
        )
    }
}
