package dev.koga.deeplinklauncher.screen.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.DLLHorizontalDivider
import dev.koga.deeplinklauncher.screen.HomeTabPage
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeTabRow(
    pagerState: PagerState,
) {
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = {
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(it[pagerState.currentPage])
                    .width(1.dp)
                    .height(4.dp)
//                    .padding(horizontal = 28.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape),
            )
        },
        divider = {
            DLLHorizontalDivider(
                thickness = .2.dp,
            )
        },
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        HomeTabPage.entries.forEach {
            val selected = it.ordinal == pagerState.currentPage
            Tab(
                selected = selected,
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
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                },
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedContentColor = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
