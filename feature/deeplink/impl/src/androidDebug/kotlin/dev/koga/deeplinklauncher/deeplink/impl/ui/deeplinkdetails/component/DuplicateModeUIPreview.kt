package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink.Companion.previewFavorite
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink.Companion.previewNotFavorite
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DuplicateModeUIFavoritePreview() {
    DLLPreviewTheme {
        DuplicateModeUI(
            uiState = DeepLinkDetailsUiState.Duplicate(
                deepLink = previewFavorite,
            ),
            onAction = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DuplicateModeUINotFavoritePreview() {
    DLLPreviewTheme {
        DuplicateModeUI(
            uiState = DeepLinkDetailsUiState.Duplicate(
                deepLink = previewNotFavorite,
            ),
            onAction = {},
        )
    }
}

@Preview
@PreviewScreenSizes
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

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun TopBarPreview() {
    DLLPreviewTheme {
        TopBar(
            onBack = {},
        )
    }
}
