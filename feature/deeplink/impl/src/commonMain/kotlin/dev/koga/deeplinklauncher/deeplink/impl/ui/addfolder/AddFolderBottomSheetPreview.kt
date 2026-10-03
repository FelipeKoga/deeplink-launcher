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

@PreviewLightDark
@Composable
internal fun AddFolderBottomSheetContentErrorPreview() {
    DLLPreviewTheme {
        AddFolderBottomSheetContent(
            uiState = AddFolderUiState(
                name = "Work",
                description = "Folder Description",
                isSubmitEnabled = true,
                errorMessage = "A folder with this name already exists",
            ),
            onNameChanged = {},
            onDescriptionChanged = {},
            onSubmit = {},
        )
    }
}
