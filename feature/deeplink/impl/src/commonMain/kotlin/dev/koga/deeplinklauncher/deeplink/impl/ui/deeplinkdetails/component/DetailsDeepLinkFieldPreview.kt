package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewDeepLinkDetails
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewFolder
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewFolderOneDeepLinkCount
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.collections.immutable.persistentListOf

@PreviewLightDark
@Composable
internal fun DetailsDeepLinkInfoExpandedPreview() {
    DLLPreviewTheme {
        DetailsDeepLinkInfo(
            uiState = DeepLinkDetailsUiState.Launch(
                details = previewDeepLinkDetails,
            ),
            onCopyLink = {},
            initiallyExpanded = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun DetailsDeepLinkInfoFolderPickerPreview() {
    DLLPreviewTheme {
        DetailsDeepLinkInfo(
            uiState = DeepLinkDetailsUiState.Launch(
                details = previewDeepLinkDetails,
                folders = persistentListOf(
                    previewFolder,
                    previewFolderOneDeepLinkCount,
                ),
            ),
            onCopyLink = {},
            initiallyFolderPickerExpanded = true,
        )
    }
}
