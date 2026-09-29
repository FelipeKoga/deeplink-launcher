package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewFavoriteDeepLink
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewLightDark
@Composable
internal fun DuplicateModeUIDefaultPreview() {
    DLLPreviewTheme {
        DuplicateModeUI(
            uiState = DeepLinkDetailsUiState.Duplicate(
                deepLink = previewFavoriteDeepLink,
            ),
            onAction = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun DuplicateModeUIErrorPreview() {
    DLLPreviewTheme {
        DuplicateModeUI(
            uiState = DeepLinkDetailsUiState.Duplicate(
                deepLink = DeepLink.empty,
                errorMessage = "Something went wrong",
            ),
            onAction = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun TopBarPreview() {
    DLLPreviewTheme {
        TopBar(
            onBack = {},
        )
    }
}
