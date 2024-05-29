package dev.koga.deeplinklauncher.screen.component

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
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier,
    allDeepLinks: ImmutableList<DeepLink>,
    favoriteDeepLinks: ImmutableList<DeepLink>,
    folders: ImmutableList<Folder>,
    pagerState: PagerState,
    allDeepLinksListState: LazyStaggeredGridState,
    favoritesDeepLinksListState: LazyStaggeredGridState,
    scrollBehavior: TopAppBarScrollBehavior,
    paddingBottom: Dp,
    onDeepLinkClicked: (DeepLink) -> Unit,
    onDeepLinkLaunch: (DeepLink) -> Unit,
    onFolderClicked: (Folder) -> Unit,
    onFolderAdd: () -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
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

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
)
@Composable
fun DeepLinksLazyColumn(
    deepLinks: List<DeepLink>,
    scrollBehavior: TopAppBarScrollBehavior,
    paddingBottom: Dp,
    onClick: (DeepLink) -> Unit,
    onLaunch: (DeepLink) -> Unit,
    onFolderClicked: (Folder) -> Unit,
    deepLinksListState: LazyStaggeredGridState,
) {
    HomeVerticalGridList(
        state = deepLinksListState,
        scrollBehavior = scrollBehavior,
        paddingBottom = paddingBottom,
    ) {
        items(
            count = deepLinks.size,
            key = { deepLinks[it].id },
        ) { index ->
            val deepLink = deepLinks[index]

            DeepLinkCard(
                modifier = Modifier
                    .animateItemPlacement(),
                deepLink = deepLink,
                onClick = { onClick(deepLink) },
                onLaunch = { onLaunch(deepLink) },
                onFolderClicked = { onFolderClicked(deepLink.folder!!) },
            )
        }
    }
}


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun FoldersVerticalStaggeredGrid(
    folders: ImmutableList<Folder>,
    scrollBehavior: TopAppBarScrollBehavior,
    paddingBottom: Dp,
    onAdd: () -> Unit,
    onClick: (Folder) -> Unit,
) {
    val windowSizeClass = calculateWindowSizeClass()
    val numberOfColumns = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> 3
        else -> 2
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(numberOfColumns),
        state = rememberLazyStaggeredGridState(),
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(
            top = 12.dp,
            start = 12.dp,
            end = 12.dp,
            bottom = paddingBottom,
        ),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalItemSpacing = 24.dp
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

        items(folders.size, key = { folders[it].id }) { index ->
            FolderCard(
                folder = folders[index],
                onClick = { onClick(it) },
                modifier = Modifier.animateItemPlacement(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HomeVerticalGridList(
    modifier: Modifier = Modifier,
    state: LazyStaggeredGridState,
    scrollBehavior: TopAppBarScrollBehavior,
    paddingBottom: Dp,
    content: LazyStaggeredGridScope.() -> Unit,
) {
    val windowSizeClass = calculateWindowSizeClass()

    val numberOfColumns = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Medium -> 2
        WindowWidthSizeClass.Expanded -> 3
        else -> 1
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(numberOfColumns),
        state = state,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(
            top = 12.dp,
            start = 12.dp,
            end = 12.dp,
            bottom = paddingBottom,
        ),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalItemSpacing = 24.dp
    ) {
        content()
    }
}
