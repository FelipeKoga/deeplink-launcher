package dev.koga.deeplinklauncher.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import dev.koga.deeplinklauncher.home.HomeTabPage
import kotlinx.coroutines.launch

@Composable
internal fun HomeTabRow(
    pagerState: PagerState,
) {
    val scope = rememberCoroutineScope()
    val colors = DeepLinkTheme.colors
    val shapes = DeepLinkTheme.shapes

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = {
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(it[pagerState.currentPage])
                    .fillMaxWidth(.3f)
                    .height(2.dp)
                    .background(color = colors.surface.primary, shape = CircleShape),
            )
        },
        divider = {
            DLLHorizontalDivider()
        },
        containerColor = Color.Transparent,
        contentColor = colors.text.muted,
    ) {
        HomeTabPage.entries.forEach {
            val selected = it.ordinal == pagerState.currentPage
            Tab(
                selected = selected,
                modifier = Modifier.clip(shapes.tab),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(it.ordinal)
                    }
                },
                text = {
                    Text(
                        modifier = Modifier,
                        text = when (it) {
                            HomeTabPage.HISTORY -> "History"
                            HomeTabPage.FAVORITES -> "Favorites"
                            HomeTabPage.FOLDERS -> "Folders"
                        },
                        style = DeepLinkTheme.typography.action.tab,
                    )
                },
                unselectedContentColor = colors.text.muted,
                selectedContentColor = colors.text.primary,
            )
        }
    }
}
