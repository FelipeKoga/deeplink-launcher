package dev.koga.deeplinklauncher.settings.impl.ui.deletedata

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewLightDark
@Composable
internal fun DeleteDataBottomSheetContentPreview() {
    DLLPreviewTheme {
        DeleteDataBottomSheetContent(
            onDelete = {},
        )
    }
}
