package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewLightDark
@Composable
internal fun DeleteFolderBottomSheetPreview() {
    DLLPreviewTheme {
        DeleteFolderBottomSheet(
            onDismissRequest = {},
            onDelete = {},
        )
    }
}
