package dev.koga.deeplinklauncher.home.component

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.home.HomeTabPage
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun HomeTopBarTitlePreview() {
    DLLPreviewTheme {
        HomeTopBarTitle()
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun HomeTopBarPreview() {
    DLLPreviewTheme {
        HomeTopBar(
            search = "Search",
            onSettingsScreen = {},
            onSearch = {},
            pagerState = rememberPagerState(
                initialPage = HomeTabPage.HISTORY.ordinal,
                pageCount = { HomeTabPage.entries.size },
            ),
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun HomeSearchBarPreview() {
    DLLPreviewTheme {
        HomeSearchBar(
            value = "Search",
            onSearch = {},
            onClose = {},
        )
    }
}
