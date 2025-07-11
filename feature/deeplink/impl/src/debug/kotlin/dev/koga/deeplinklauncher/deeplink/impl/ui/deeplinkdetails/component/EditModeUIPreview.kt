package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink.Companion.previewFavorite
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink.Companion.previewNotFavorite
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun EditModeUIFavoritePreview() {
    DLLPreviewTheme {
        EditModeUI(
            uiState = DeepLinkDetailsUiState.Edit(
                deepLink = previewFavorite,
                folders = persistentListOf(),
            ),
            onAction = {},
            onShowDeleteConfirmation = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun EditModeUINotFavoritePreview() {
    DLLPreviewTheme {
        EditModeUI(
            uiState = DeepLinkDetailsUiState.Edit(
                deepLink = previewNotFavorite,
                folders = persistentListOf(),
            ),
            onAction = {},
            onShowDeleteConfirmation = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun EditModeUIErrorPreview() {
    DLLPreviewTheme {
        EditModeUI(
            uiState = DeepLinkDetailsUiState.Edit(
                deepLink = DeepLink.empty,
                folders = persistentListOf(),
                errorMessage = "Something went wrong"
            ),
            onAction = {},
            onShowDeleteConfirmation = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DeepLinkDetailsTextFieldPreview() {
    DLLPreviewTheme {
        DeepLinkDetailsTextField(
            text = "Sample Text",
            onTextChange = {},
            label = "Label"
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun EditTopBarPreview() {
    DLLPreviewTheme {
        EditTopBar(
            onBack = {},
            onDelete = {}
        )
    }
}