package dev.koga.deeplinklauncher.home.impl.ui.component

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.home.impl.ui.HomeTabPage

@PreviewLightDark
@Composable
internal fun HomeTopBarTitlePreview() {
    DLLPreviewTheme {
        HomeTopBarTitle()
    }
}

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

@PreviewLightDark
@Composable
internal fun HomeTopBarSearchingPreview() {
    DLLPreviewTheme {
        HomeTopBar(
            search = "github",
            onSettingsScreen = {},
            onSearch = {},
            pagerState = rememberPagerState(
                initialPage = HomeTabPage.HISTORY.ordinal,
                pageCount = { HomeTabPage.entries.size },
            ),
            initiallySearching = true,
        )
    }
}
