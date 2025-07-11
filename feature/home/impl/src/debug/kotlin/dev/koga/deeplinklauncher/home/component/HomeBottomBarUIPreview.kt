package dev.koga.deeplinklauncher.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.model.Suggestion
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.home.state.DeepLinkInputState
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun HomeBottomBarUIPreview() {
    DLLPreviewTheme {
        HomeBottomBarUI(
            state = DeepLinkInputState(
                text = "Text",
            ),
            onValueChange = {},
            launch = {},
            onSuggestionClicked = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun HomeBottomBarUIErrorPreview() {
    DLLPreviewTheme {
        HomeBottomBarUI(
            state = DeepLinkInputState(
                text = "Text",
                errorMessage = "Something went wrong"
            ),
            onValueChange = {},
            launch = {},
            onSuggestionClicked = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun HomeBottomBarUIWithSuggestionsPreview() {
    DLLPreviewTheme {
        HomeBottomBarUI(
            state = DeepLinkInputState(
                text = "Text",
                suggestions = persistentListOf(
                    Suggestion.Clipboard("Clipboard 1"),
                    Suggestion.Clipboard("Clipboard 2"),
                    Suggestion.History("History 1"),
                    Suggestion.History("History 2"),
                )
            ),
            onValueChange = {},
            launch = {},
            onSuggestionClicked = {}
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun SuggestionListItemPreview() {
    DLLPreviewTheme {
        SuggestionListItem(
            visible = true,
            suggestion = Suggestion.Clipboard("Clipboard 1"),
        )
    }
}