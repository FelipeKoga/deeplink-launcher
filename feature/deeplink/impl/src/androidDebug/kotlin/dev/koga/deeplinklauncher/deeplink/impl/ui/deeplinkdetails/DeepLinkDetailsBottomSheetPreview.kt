package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkDetailsModel
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewFavoriteDeepLink
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewFolder
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewFolderOneDeepLinkCount
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

private val previewMetadata = DeepLinkMetadata(
    scheme = "https",
    host = "google.com",
    path = "/",
    query = null,
)

private val previewHandlerInfo = DeepLinkHandlerInfo(
    canResolve = true,
    appName = "Chrome",
)

private val previewDetails = DeepLinkDetailsModel(
    deepLink = previewFavoriteDeepLink,
    metadata = previewMetadata,
    handlerInfo = previewHandlerInfo,
)

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DeepLinkDetailsLaunchUIPreview() {
    DLLPreviewTheme {
        DeepLinkDetailsUI(
            uiState = DeepLinkDetailsUiState.Launch(
                details = previewDetails,
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
internal fun DeepLinkDetailsUIPreview() {
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
