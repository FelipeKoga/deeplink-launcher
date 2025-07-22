package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink.Companion.previewFavorite
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

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
                    Folder.previewOneDeepLinkCount
                ),
            ),
            onAction = {},
            onShowDeleteConfirmation = {}
        )
    }
}