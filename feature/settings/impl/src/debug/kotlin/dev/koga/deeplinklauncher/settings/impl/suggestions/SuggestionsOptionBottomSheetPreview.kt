package dev.koga.deeplinklauncher.settings.impl.suggestions

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
internal fun SuggestionsOptionBottomSheetPreview() {
    DLLPreviewTheme {
        SuggestionsOptionBottomSheet(
            viewModel = koinViewModel(),
            onDismissRequest = {}
        )
    }
}