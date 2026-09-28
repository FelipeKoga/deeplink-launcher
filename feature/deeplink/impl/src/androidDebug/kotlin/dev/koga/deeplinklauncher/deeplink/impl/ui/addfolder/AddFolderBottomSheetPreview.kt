package dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder.state.AddFolderUiState
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewLightDark
@Composable
internal fun AddFolderBottomSheetContentPreview() {
    DLLPreviewTheme {
        AddFolderBottomSheetContent(
            uiState = AddFolderUiState(
                name = "Folder Name",
                description = "Folder Description",
                isSubmitEnabled = false,
            ),
            onNameChanged = {},
            onDescriptionChanged = {},
            onSubmit = {},
        )
    }
}
