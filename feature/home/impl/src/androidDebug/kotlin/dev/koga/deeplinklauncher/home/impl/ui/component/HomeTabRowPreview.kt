package dev.koga.deeplinklauncher.home.impl.ui.component

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.home.impl.ui.HomeTabPage

@PreviewLightDark
@Composable
internal fun HomeTabRowPreview() {
    DLLPreviewTheme {
        HomeTabRow(
            pagerState = rememberPagerState(
                initialPage = HomeTabPage.HISTORY.ordinal,
                pageCount = { HomeTabPage.entries.size },
            ),
        )
    }
}
