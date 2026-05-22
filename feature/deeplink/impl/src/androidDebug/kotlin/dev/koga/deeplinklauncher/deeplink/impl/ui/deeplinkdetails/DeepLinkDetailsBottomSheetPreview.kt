package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink.Companion.previewFavorite
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

private val previewMetadata = DeepLinkMetadata(
    link = "https://google.com",
    scheme = "https",
    host = "google.com",
    path = "/",
    query = null,
)

private val previewHandlerInfo = DeepLinkHandlerInfo(
    canResolve = true,
    appName = "Chrome",
)

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DeepLinkDetailsLaunchUIPreview() {
    DLLPreviewTheme {
        DeepLinkDetailsUI(
            uiState = DeepLinkDetailsUiState.Launch(
                deepLink = previewFavorite,
                icon = null,
                metadata = previewMetadata,
                handlerInfo = previewHandlerInfo,
            ),
            onAction = {},
            onShowDeleteConfirmation = {},
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
                deepLink = previewFavorite,
                folders = persistentListOf(
                    Folder.preview,
                    Folder.previewOneDeepLinkCount,
                ),
            ),
            onAction = {},
            onShowDeleteConfirmation = {},
        )
    }
}
