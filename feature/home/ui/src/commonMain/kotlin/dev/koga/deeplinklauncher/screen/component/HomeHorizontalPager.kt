package dev.koga.deeplinklauncher.screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.screen.HomeTabPage
import dev.koga.deeplinklauncher.deeplink.DeepLinkItem
import dev.koga.deeplinklauncher.folder.FolderCard
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun HomeHorizontalPager(
    allDeepLinks: ImmutableList<DeepLink>,
    favoriteDeepLinks: ImmutableList<DeepLink>,
    folders: ImmutableList<Folder>,
    pagerState: PagerState,
    scrollBehavior: TopAppBarScrollBehavior,
    paddingBottom: Dp,
    onDeepLinkClicked: (DeepLink) -> Unit,
    onDeepLinkLaunch: (DeepLink) -> Unit,
    onDeepLinkCopy: (DeepLink) -> Unit,
    onFolderClicked: (Folder) -> Unit,
    onFolderAdd: () -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
    ) { page ->
        when (page) {
            HomeTabPage.HISTORY.ordinal -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        end = 12.dp,
                        top = 12.dp,
                        bottom = paddingBottom,
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        key = { it.id },
                        items = allDeepLinks,
                    ) { deepLink ->
                        DeepLinkItem(
                            deepLink = deepLink,
                            onClick = { onDeepLinkClicked(deepLink) },
                            onLaunch = { onDeepLinkLaunch(deepLink) },
                            onCopy = { onDeepLinkCopy(it) },
                        )
                    }
                }
            }

            HomeTabPage.FAVORITES.ordinal -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        end = 12.dp,
                        top = 12.dp,
                        bottom = paddingBottom,
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        key = { it.id },
                        items = favoriteDeepLinks,
                    ) { deepLink ->
                        DeepLinkItem(
                            deepLink = deepLink,
                            onClick = { onDeepLinkClicked(deepLink) },
                            onLaunch = { onDeepLinkLaunch(deepLink) },
                            onCopy = { onDeepLinkCopy(deepLink) },
                        )
                    }
                }
            }

            HomeTabPage.FOLDERS.ordinal -> {
                Box(modifier = Modifier.fillMaxSize()) {
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
                                onClick = onFolderAdd,
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
                                onClick = { onFolderClicked(it) },
                            )
                        }
                    }
                }
            }
        }
    }
}
