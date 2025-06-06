package dev.koga.deeplinklauncher.home.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.koga.deeplinklauncher.home.ui.component.DeepLinksLazyColumn
import dev.koga.deeplinklauncher.home.ui.component.FoldersVerticalStaggeredGrid
import dev.koga.deeplinklauncher.home.ui.component.HomeBottomBarUI
import dev.koga.deeplinklauncher.home.ui.component.HomeTopBar
import dev.koga.deeplinklauncher.home.ui.state.HomeUiState
import dev.koga.deeplinklauncher.navigation.AppNavigationRoute
import dev.koga.deeplinklauncher.navigation.AppNavigator

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    appNavigator: AppNavigator,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.showOnboarding) {
        if (uiState.showOnboarding) {
            appNavigator.navigate(AppNavigationRoute.Onboarding)
        }
    }

    HomeUI(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun HomeUI(
    uiState: HomeUiState,
    onAction: (HomeAction) -> Unit,
    hazeState: HazeState = remember { HazeState() },
    historyListState: LazyGridState = rememberLazyGridState(),
    favoritesListState: LazyGridState = rememberLazyGridState(),
    pagerState: PagerState = rememberPagerState(
        initialPage = HomeTabPage.HISTORY.ordinal,
        pageCount = { HomeTabPage.entries.size },
    ),
) {
    LaunchedEffect(uiState.searchInput) {
        if (uiState.deepLinks.isNotEmpty()) {
            historyListState.animateScrollToItem(index = 0)
            favoritesListState.animateScrollToItem(index = 0)
        }
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                search = uiState.searchInput,
                onSettingsScreen = {
                    onAction(HomeAction.Navigate(AppNavigationRoute.Settings.Root))
                },
                onSearch = { onAction(HomeAction.Search(it)) },
                pagerState = pagerState,
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.regular(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                ),
            )
        },
        bottomBar = {
            HomeBottomBarUI(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.thick(),
                    )
                    .navigationBarsPadding(),
                state = uiState.deepLinkInputState,
                launch = { onAction(HomeAction.LaunchInputDeepLink) },
                onSuggestionClicked = { onAction(HomeAction.OnSuggestionClicked(it)) },
                onValueChange = { onAction(HomeAction.OnInputChanged(it)) },
            )
        },
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            when (page) {
                HomeTabPage.HISTORY.ordinal,
                HomeTabPage.FAVORITES.ordinal,
                -> DeepLinksLazyColumn(
                    modifier = Modifier.hazeSource(hazeState),
                    listState = historyListState,
                    deepLinks = uiState.deepLinks,
                    contentPadding = it,
                    onClick = {
                        onAction(
                            HomeAction.Navigate(AppNavigationRoute.DeepLinkDetails(it.id, true)),
                        )
                    },
                    onLaunch = {
                        onAction(HomeAction.LaunchDeepLink(it))
                    },
                    onFolderClicked = {
                        onAction(HomeAction.Navigate(AppNavigationRoute.FolderDetails(it.id)))
                    },
                )

                HomeTabPage.FOLDERS.ordinal -> FoldersVerticalStaggeredGrid(
                    modifier = Modifier.hazeSource(hazeState),
                    contentPadding = it,
                    folders = uiState.folders,
                    onClick = {
                        onAction(HomeAction.Navigate(AppNavigationRoute.FolderDetails(it.id)))
                    },
                    onAdd = { onAction(HomeAction.Navigate(AppNavigationRoute.AddFolder)) },
                )
            }
        }
    }
}
