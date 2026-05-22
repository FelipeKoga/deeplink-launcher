package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink.Companion.previewFavorite
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink.Companion.previewNotFavorite
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
internal fun LaunchModeUIFavoritePreview() {
    DLLPreviewTheme {
        LaunchModeUI(
            uiState = DeepLinkDetailsUiState.Launch(
                deepLink = previewFavorite,
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
internal fun LaunchModeUINotFavoritePreview() {
    DLLPreviewTheme {
        LaunchModeUI(
            uiState = DeepLinkDetailsUiState.Launch(
                deepLink = previewNotFavorite,
                metadata = previewMetadata,
                handlerInfo = previewHandlerInfo,
            ),
            onAction = {},
            onShowDeleteConfirmation = {},
        )
    }
}

@Preview
@PreviewLightDark
@Composable
internal fun DetailsQuickActionsGridPreview() {
    DLLPreviewTheme {
        DetailsQuickActions(
            isFavorite = false,
            onAction = {},
            onShowDeleteConfirmation = {},
        )
    }
}

@Preview
@PreviewLightDark
@Composable
internal fun DetailsQuickActionsGridFavoritePreview() {
    DLLPreviewTheme {
        DetailsQuickActions(
            isFavorite = true,
            onAction = {},
            onShowDeleteConfirmation = {},
        )
    }
}

@Preview
@PreviewLightDark
@Composable
internal fun DetailsHeaderPreview() {
    DLLPreviewTheme {
        DetailsHeader(
            deepLink = previewFavorite,
            icon = null,
            metadataHost = previewMetadata.host,
            createdAt = previewFavorite.createdAt,
        )
    }
}

@Preview
@PreviewLightDark
@Composable
internal fun DetailsDeepLinkFieldPreview() {
    DLLPreviewTheme {
        DetailsDeepLinkField(
            link = previewMetadata.link,
            metadata = previewMetadata,
            handlerInfo = previewHandlerInfo,
            icon = null,
            description = previewFavorite.description,
            onCopyLink = {},
        )
    }
}

@Preview
@PreviewLightDark
@Composable
internal fun DetailsDeepLinkFieldWithFolderPreview() {
    DLLPreviewTheme {
        DetailsDeepLinkField(
            link = previewMetadata.link,
            metadata = previewMetadata,
            handlerInfo = previewHandlerInfo,
            icon = null,
            folder = Folder.preview,
            onCopyLink = {},
            onFolderClick = {},
        )
    }
}

@Preview
@PreviewLightDark
@Composable
internal fun DetailsDeepLinkFieldWithoutFolderPreview() {
    DLLPreviewTheme {
        DetailsDeepLinkField(
            link = previewMetadata.link,
            metadata = previewMetadata,
            handlerInfo = previewHandlerInfo,
            icon = null,
            folders = persistentListOf(
                Folder.preview,
                Folder.previewOneDeepLinkCount,
            ),
            onCopyLink = {},
            onToggleFolder = {},
            onAddFolder = {},
        )
    }
}
