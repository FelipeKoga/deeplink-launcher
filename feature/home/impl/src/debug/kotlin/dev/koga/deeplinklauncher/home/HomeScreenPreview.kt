package dev.koga.deeplinklauncher.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.model.Suggestion
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.home.state.DeepLinkInputState
import dev.koga.deeplinklauncher.home.state.HomeUiState
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun HomeUIPreview() {
    DLLPreviewTheme {
        HomeUI(
            uiState = HomeUiState(
                deepLinks = persistentListOf(
                    DeepLink.previewFavorite,
                    DeepLink.previewNotFavorite,
                    DeepLink.previewNotFavorite,
                ),
                favorites = persistentListOf(
                    DeepLink.previewFavorite,
                    DeepLink.previewNotFavorite,
                    DeepLink.previewNotFavorite,
                ),
                folders = persistentListOf(
                    Folder.preview,
                    Folder.previewOneDeepLinkCount,
                    Folder.previewOneDeepLinkCount,
                ),
                deepLinkInputState = DeepLinkInputState(
                    text = "https://example.com",
                ),
                searchInput = "Search",
                showOnboarding = true
            ),
            onAction = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun HomeUIEmptyItemsPreview() {
    DLLPreviewTheme {
        HomeUI(
            uiState = HomeUiState(
                deepLinks = persistentListOf(),
                favorites = persistentListOf(),
                folders = persistentListOf(),
                deepLinkInputState = DeepLinkInputState(
                    text = "https://example.com",
                ),
                searchInput = "Search",
                showOnboarding = false
            ),
            onAction = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun HomeUIEmptyDeepLinkInputErrorPreview() {
    DLLPreviewTheme {
        HomeUI(
            uiState = HomeUiState(
                deepLinks = persistentListOf(),
                favorites = persistentListOf(),
                folders = persistentListOf(),
                deepLinkInputState = DeepLinkInputState(
                    text = "https://example.com",
                    errorMessage = "Something went wrong"
                ),
                searchInput = "Search",
                showOnboarding = true
            ),
            onAction = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun HomeUIEmptyDeepLinkInputSuggestionsPreview() {
    DLLPreviewTheme {
        HomeUI(
            uiState = HomeUiState(
                deepLinks = persistentListOf(),
                favorites = persistentListOf(),
                folders = persistentListOf(),
                deepLinkInputState = DeepLinkInputState(
                    text = "https://example.com",
                    errorMessage = "Something went wrong",
                    suggestions = persistentListOf(
                        Suggestion.Clipboard("Clipboard 1"),
                        Suggestion.Clipboard("Clipboard 2"),
                        Suggestion.History("History 1"),
                        Suggestion.History("History 2"),
                    )
                ),
                searchInput = "Search",
                showOnboarding = true
            ),
            onAction = {},
        )
    }
}