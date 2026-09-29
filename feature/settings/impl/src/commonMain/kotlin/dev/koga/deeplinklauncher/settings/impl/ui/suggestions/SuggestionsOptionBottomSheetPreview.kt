package dev.koga.deeplinklauncher.settings.impl.ui.suggestions

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewLightDark
@Composable
internal fun SuggestionsOptionBottomSheetContentEnabledPreview() {
    DLLPreviewTheme {
        SuggestionsOptionBottomSheetContent(
            enabled = true,
            onEnabledChange = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun SuggestionsOptionBottomSheetContentDisabledPreview() {
    DLLPreviewTheme {
        SuggestionsOptionBottomSheetContent(
            enabled = false,
            onEnabledChange = {},
        )
    }
}
