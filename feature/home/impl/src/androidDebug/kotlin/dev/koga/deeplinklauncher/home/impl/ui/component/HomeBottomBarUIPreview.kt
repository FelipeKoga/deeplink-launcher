package dev.koga.deeplinklauncher.home.impl.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.ui.DeepLinkInputState
import dev.koga.deeplinklauncher.deeplink.ui.DeepLinkLaunchBottomBar
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.domain.deeplink.api.model.Suggestion
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun HomeBottomBarUIPreview() {
    DLLPreviewTheme {
        DeepLinkLaunchBottomBar(
            state = DeepLinkInputState(
                text = "Text",
            ),
            onValueChange = {},
            launch = {},
            onSuggestionClicked = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun HomeBottomBarUIErrorPreview() {
    DLLPreviewTheme {
        DeepLinkLaunchBottomBar(
            state = DeepLinkInputState(
                text = "Text",
                errorMessage = "Something went wrong",
            ),
            onValueChange = {},
            launch = {},
            onSuggestionClicked = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun HomeBottomBarUIWithSuggestionsPreview() {
    DLLPreviewTheme {
        DeepLinkLaunchBottomBar(
            state = DeepLinkInputState(
                text = "Text",
                suggestions = persistentListOf(
                    Suggestion.Clipboard("Clipboard 1"),
                    Suggestion.Clipboard("Clipboard 2"),
                    Suggestion.History("History 1"),
                    Suggestion.History("History 2"),
                ),
            ),
            onValueChange = {},
            launch = {},
            onSuggestionClicked = {},
        )
    }
}
