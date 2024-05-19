package dev.koga.deeplinklauncher.screen.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.deeplink.DeepLinkCard
import dev.koga.deeplinklauncher.folder.FolderCard
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.screen.HomeTabPage
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun HomeHorizontalPager(
    allDeepLinks: ImmutableList<DeepLink>,
    favoriteDeepLinks: ImmutableList<DeepLink>,
    folders: ImmutableList<Folder>,
    pagerState: PagerState,
    allDeepLinksListState: LazyListState,
    favoritesDeepLinksListState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
    paddingBottom: Dp,
    onDeepLinkClicked: (DeepLink) -> Unit,
    onDeepLinkLaunch: (DeepLink) -> Unit,
    onFolderClicked: (Folder) -> Unit,
    onFolderAdd: () -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
    ) { page ->
        when (page) {
            HomeTabPage.HISTORY.ordinal -> DeepLinksLazyColumn(
                deepLinksListState = allDeepLinksListState,
                deepLinks = allDeepLinks,
                scrollBehavior = scrollBehavior,
                paddingBottom = paddingBottom,
                onClick = onDeepLinkClicked,
                onLaunch = onDeepLinkLaunch,
                onFolderClicked = onFolderClicked,
            )

            HomeTabPage.FAVORITES.ordinal -> DeepLinksLazyColumn(
                deepLinksListState = favoritesDeepLinksListState,
                deepLinks = favoriteDeepLinks,
                scrollBehavior = scrollBehavior,
                paddingBottom = paddingBottom,
                onClick = onDeepLinkClicked,
                onLaunch = onDeepLinkLaunch,
                onFolderClicked = onFolderClicked,
            )

            HomeTabPage.FOLDERS.ordinal -> FoldersVerticalStaggeredGrid(
                folders = folders,
                scrollBehavior = scrollBehavior,
                paddingBottom = paddingBottom,
                onAdd = onFolderAdd,
                onClick = onFolderClicked,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DeepLinksLazyColumn(
    deepLinks: List<DeepLink>,
    scrollBehavior: TopAppBarScrollBehavior,
    paddingBottom: Dp,
    onClick: (DeepLink) -> Unit,
    onLaunch: (DeepLink) -> Unit,
    onFolderClicked: (Folder) -> Unit,
    deepLinksListState: LazyListState,
) {

    LazyColumn(
        state = deepLinksListState,
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(
            bottom = paddingBottom,
        ),
    ) {
        items(
            key = { it.id },
            items = deepLinks,
        ) { deepLink ->
            DeepLinkCard(
                modifier = Modifier.animateItemPlacement(),
                deepLink = deepLink,
                onClick = { onClick(deepLink) },
                onLaunch = { onLaunch(deepLink) },
                onFolderClicked = { onFolderClicked(deepLink.folder!!) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FoldersVerticalStaggeredGrid(
    folders: ImmutableList<Folder>,
    scrollBehavior: TopAppBarScrollBehavior,
    paddingBottom: Dp,
    onAdd: () -> Unit,
    onClick: (Folder) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(
            start = 12.dp,
            end = 12.dp,
            top = 24.dp,
            bottom = paddingBottom,
        ),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalItemSpacing = 24.dp,
        columns = StaggeredGridCells.Fixed(2),
    ) {
        item {
            OutlinedCard(
                onClick = onAdd,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .3f),
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(184.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Create new folder",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
            }
        }

        items(folders.size) { index ->
            FolderCard(
                folder = folders[index],
                onClick = { onClick(it) },
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}
