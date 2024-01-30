package dev.koga.deeplinklauncher.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.deeplinklauncher.HomeTabPage
import dev.koga.resources.MR
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeTabRow(
    pagerState: PagerState,
) {
    val scope = rememberCoroutineScope()

    TabRow(selectedTabIndex = pagerState.currentPage) {
        Tab(
            selected = pagerState.currentPage == HomeTabPage.HISTORY.ordinal,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(HomeTabPage.HISTORY.ordinal)
                }
            },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(MR.images.ic_history_24dp),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "History")
                }
            },
        )
        Tab(
            selected = pagerState.currentPage == HomeTabPage.FAVORITES.ordinal,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(HomeTabPage.FAVORITES.ordinal)
                }
            },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Favorites")
                }
            },
        )
        Tab(
            selected = pagerState.currentPage == HomeTabPage.FOLDERS.ordinal,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(HomeTabPage.FOLDERS.ordinal)
                }
            },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(MR.images.ic_folder_24dp),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Folders")
                }
            },

        )
    }
}
