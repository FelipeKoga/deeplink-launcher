package dev.koga.deeplinklauncher.settings.impl.deletedata

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DeleteDataBottomSheetPreview() {
    DLLPreviewTheme {
        DeleteDataBottomSheet(
            viewModel = koinViewModel(),
            onDismissRequest = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DeleteDataBottomSheetContentPreview() {
    DLLPreviewTheme {
        DeleteDataBottomSheetContent(
            onDelete = {}
        )
    }
}