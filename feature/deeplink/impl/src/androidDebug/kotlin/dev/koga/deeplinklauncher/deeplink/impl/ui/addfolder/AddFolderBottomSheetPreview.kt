package dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
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
