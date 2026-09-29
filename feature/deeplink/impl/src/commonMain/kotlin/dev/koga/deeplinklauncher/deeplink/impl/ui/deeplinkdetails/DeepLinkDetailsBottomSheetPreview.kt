package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewDeepLinkDetails
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewFavoriteDeepLink
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewFolder
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewFolderOneDeepLinkCount
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.collections.immutable.persistentListOf

@PreviewLightDark
@Composable
internal fun DeepLinkDetailsUILaunchPreview() {
    DLLPreviewTheme {
        DeepLinkDetailsUI(
            uiState = DeepLinkDetailsUiState.Launch(
                details = previewDeepLinkDetails,
            ),
            onAction = {},
            onShowDeleteConfirmation = {},
            scrollState = rememberScrollState(),
        )
    }
}

@PreviewLightDark
@Composable
internal fun DeepLinkDetailsUIEditPreview() {
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
