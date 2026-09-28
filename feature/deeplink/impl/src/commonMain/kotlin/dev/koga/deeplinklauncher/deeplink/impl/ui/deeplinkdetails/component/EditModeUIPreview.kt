package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewFavoriteDeepLink
import dev.koga.deeplinklauncher.deeplink.impl.ui.preview.previewTargetAppHandlers
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@PreviewLightDark
@Composable
internal fun EditModeUIDefaultPreview() {
    DLLPreviewTheme {
        EditModeUI(
            uiState = DeepLinkDetailsUiState.Edit(
                deepLink = previewFavoriteDeepLink,
                folders = persistentListOf(),
            ),
            onAction = {},
            onShowDeleteConfirmation = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun EditModeUIErrorPreview() {
    DLLPreviewTheme {
        EditModeUI(
            uiState = DeepLinkDetailsUiState.Edit(
                deepLink = DeepLink.empty,
                folders = persistentListOf(),
                errorMessage = "Something went wrong",
            ),
            onAction = {},
            onShowDeleteConfirmation = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun EditModeUITargetAppPreview() {
    DLLPreviewTheme {
        EditModeUI(
            uiState = DeepLinkDetailsUiState.Edit(
                deepLink = previewFavoriteDeepLink.copy(
                    link = "myapp://home",
                    targetPackage = "com.example.staging",
                ),
                folders = persistentListOf(),
                availableHandlers = previewTargetAppHandlers.toPersistentList(),
            ),
            onAction = {},
            onShowDeleteConfirmation = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun DeepLinkDetailsTextFieldPreview() {
    DLLPreviewTheme {
        DeepLinkDetailsTextField(
            text = "Sample Text",
            onTextChange = {},
            label = "Label",
        )
    }
}
