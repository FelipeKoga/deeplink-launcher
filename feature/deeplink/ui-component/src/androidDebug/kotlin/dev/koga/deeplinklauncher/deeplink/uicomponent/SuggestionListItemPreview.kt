package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Suggestion
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewLightDark
@Composable
private fun SuggestionListItemClipboardPreview() {
    DLLPreviewTheme {
        SuggestionListItem(
            visible = true,
            suggestion = Suggestion.Clipboard(text = "shop://product/42?ref=clipboard"),
        )
    }
}

@PreviewLightDark
@Composable
private fun SuggestionListItemHistoryPreview() {
    DLLPreviewTheme {
        SuggestionListItem(
            visible = true,
            suggestion = Suggestion.History(text = "shop://orders/1234/tracking"),
        )
    }
}
