package dev.koga.deeplinklauncher.home.ui.screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Plus
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.component.DeepLinkCard
import dev.koga.deeplinklauncher.deeplink.ui.folder.component.FolderCard
import dev.koga.deeplinklauncher.home.ui.screen.HomeTabPage
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeHorizontalPager(
    modifier: Modifier = Modifier,
    allDeepLinks: ImmutableList<DeepLink>,
    favoriteDeepLinks: ImmutableList<DeepLink>,
    folders: ImmutableList<Folder>,
    pagerState: PagerState,
    historyListState: LazyGridState,
    favoritesListState: LazyGridState,
    scrollBehavior: TopAppBarScrollBehavior,
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
                listState = historyListState,
                deepLinks = allDeepLinks,
                scrollBehavior = scrollBehavior,
                onClick = onDeepLinkClicked,
                onLaunch = onDeepLinkLaunch,
                onFolderClicked = onFolderClicked,
            )

            HomeTabPage.FAVORITES.ordinal -> DeepLinksLazyColumn(
                listState = favoritesListState,
                deepLinks = favoriteDeepLinks,
                scrollBehavior = scrollBehavior,
                onClick = onDeepLinkClicked,
                onLaunch = onDeepLinkLaunch,
                onFolderClicked = onFolderClicked,
            )

            HomeTabPage.FOLDERS.ordinal -> FoldersVerticalStaggeredGrid(
                folders = folders,
                scrollBehavior = scrollBehavior,
                onAdd = onFolderAdd,
                onClick = onFolderClicked,
            )
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun DeepLinksLazyColumn(
    listState: LazyGridState,
    deepLinks: List<DeepLink>,
    scrollBehavior: TopAppBarScrollBehavior,
    onClick: (DeepLink) -> Unit,
    onLaunch: (DeepLink) -> Unit,
    onFolderClicked: (Folder) -> Unit,
) {
    HomeVerticalGridList(
        state = listState,
        scrollBehavior = scrollBehavior,
    ) {
        items(
            count = deepLinks.size,
            key = { deepLinks[it].id },
        ) { index ->
            val deepLink = deepLinks[index]

            DeepLinkCard(
                modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
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
    ExperimentalMaterial3WindowSizeClassApi::class,
)
@Composable
fun FoldersVerticalStaggeredGrid(
    folders: ImmutableList<Folder>,
    scrollBehavior: TopAppBarScrollBehavior,
    onAdd: () -> Unit,
    onClick: (Folder) -> Unit,
) {
    val windowSizeClass = calculateWindowSizeClass()
    val numberOfColumns = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> 3
        else -> 2
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(numberOfColumns),
        state = rememberLazyGridState(),
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        item {
            OutlinedCard(
                onClick = onAdd,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(184.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        imageVector = TablerIcons.Plus,
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
                modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HomeVerticalGridList(
    modifier: Modifier = Modifier,
    state: LazyGridState,
    scrollBehavior: TopAppBarScrollBehavior,
    content: LazyGridScope.() -> Unit,
) {
    val windowSizeClass = calculateWindowSizeClass()

    val numberOfColumns = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Medium -> 2
        WindowWidthSizeClass.Expanded -> 3
        else -> 1
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(numberOfColumns),
        state = state,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        content()
    }
}
