package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink.Companion.previewFavorite
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink.Companion.previewNotFavorite
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun LaunchModeUIFavoritePreview() {
    LaunchModeUI(
        uiState = DeepLinkDetailsUiState.Launch(
            deepLink = previewFavorite,
        ),
        onAction = {},
    )
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun LaunchModeUINotFavoritePreview() {
    LaunchModeUI(
        uiState = DeepLinkDetailsUiState.Launch(
            deepLink = previewNotFavorite,
        ),
        onAction = {},
    )
}