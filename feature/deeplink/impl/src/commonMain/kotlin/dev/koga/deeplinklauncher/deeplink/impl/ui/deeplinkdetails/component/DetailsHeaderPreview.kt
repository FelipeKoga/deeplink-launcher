package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewDeepLinkDetails
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewLightDark
@Composable
internal fun DetailsHeaderLaunchPreview() {
    DLLPreviewTheme {
        DetailsHeader(
            uiState = DeepLinkDetailsUiState.Launch(
                details = previewDeepLinkDetails,
            ),
            onEdit = {},
            onDelete = {},
        )
    }
}
