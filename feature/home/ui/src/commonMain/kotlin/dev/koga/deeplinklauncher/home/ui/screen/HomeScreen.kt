@file:OptIn(ExperimentalMaterial3Api::class)

package dev.koga.deeplinklauncher.home.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.koga.deeplinklauncher.LocalRootNavigator
import dev.koga.deeplinklauncher.SharedScreen
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.AddFolderBottomSheet
import dev.koga.deeplinklauncher.home.ui.screen.component.DeepLinksLazyColumn
import dev.koga.deeplinklauncher.home.ui.screen.component.FoldersVerticalStaggeredGrid
import dev.koga.deeplinklauncher.home.ui.screen.component.HomeLaunchDeepLinkUI
import dev.koga.deeplinklauncher.home.ui.screen.component.HomeTopBar
import dev.koga.deeplinklauncher.home.ui.screen.component.OnboardingBottomSheet
import dev.koga.deeplinklauncher.home.ui.screen.state.HomeEvent
import dev.koga.deeplinklauncher.navigateToDeepLinkDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalHazeMaterialsApi::class)
class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val navigator = LocalRootNavigator.current
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.getNavigatorScreenModel<HomeScreenModel>()
        val uiState by screenModel.uiState.collectAsState()
        val hazeState = remember { HazeState() }

        val historyListState = rememberLazyGridState()
        val favoritesListState = rememberLazyGridState()
        val pagerState = rememberPagerState(
            initialPage = HomeTabPage.HISTORY.ordinal,
            pageCount = { HomeTabPage.entries.size },
        )

        var showAddFolderBottomSheet by remember { mutableStateOf(false) }
        if (showAddFolderBottomSheet) {
            AddFolderBottomSheet(
                onDismiss = { showAddFolderBottomSheet = false },
                onAdd = { name, description ->
                    showAddFolderBottomSheet = false
                    screenModel.addFolder(name, description)
                },
            )
        }

        var showOnboardingBottomSheet by remember { mutableStateOf(false) }
        if (showOnboardingBottomSheet) {
            OnboardingBottomSheet {
                showOnboardingBottomSheet = false
                screenModel.onboardingShown()
            }
        }

        LaunchedEffect(uiState.searchInput) {
            if (uiState.deepLinks.isNotEmpty()) {
                historyListState.animateScrollToItem(index = 0)
                favoritesListState.animateScrollToItem(index = 0)
            }
        }

        HomeEventsHandler(
            events = screenModel.events,
            onDeepLinkLaunched = {
                scope.launch {
                    delay(DELAY_TO_SCROLL_TO_THE_TOP)
                    historyListState.animateScrollToItem(index = 0)
                    favoritesListState.animateScrollToItem(index = 0)
                }
            },
            onShowOnboarding = {
                showOnboardingBottomSheet = true
            },
        )

        Scaffold(
            topBar = {
                HomeTopBar(
                    search = uiState.searchInput,
                    onSettingsScreen = {
                        val settingsScreen = ScreenRegistry.get(SharedScreen.Settings)
                        navigator.push(settingsScreen)
                    },
                    onSearch = screenModel::onSearch,
                    pagerState = pagerState,
                    modifier = Modifier.hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular(containerColor = MaterialTheme.colorScheme.background),
                    ),
                )
            },
            bottomBar = {
                HomeLaunchDeepLinkUI(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .hazeEffect(
                            state = hazeState,
                            style = HazeMaterials.thick(),
                        )
                        .navigationBarsPadding(),
                    value = uiState.deepLinkInput,
                    suggestions = uiState.suggestions,
                    errorMessage = uiState.errorMessage,
                    launch = screenModel::launchDeepLink,
                    onSuggestionClicked = screenModel::onDeepLinkTextChanged,
                    onValueChange = screenModel::onDeepLinkTextChanged,
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
                        onClick = { bottomSheetNavigator.navigateToDeepLinkDetails(it.id) },
                        onLaunch = screenModel::launchDeepLink,
                        onFolderClicked = {
                            navigator.push(
                                ScreenRegistry.get(SharedScreen.FolderDetails(it.id)),
                            )
                        },
                    )

                    HomeTabPage.FOLDERS.ordinal -> FoldersVerticalStaggeredGrid(
                        modifier = Modifier.hazeSource(hazeState),
                        contentPadding = it,
                        folders = uiState.folders,
                        onClick = {
                            navigator.push(
                                ScreenRegistry.get(SharedScreen.FolderDetails(it.id)),
                            )
                        },
                        onAdd = { showAddFolderBottomSheet = true },
                    )
                }
            }
        }
    }

    companion object {
        private const val DELAY_TO_SCROLL_TO_THE_TOP = 350L
    }
}

@Composable
private fun HomeEventsHandler(
    events: Flow<HomeEvent>,
    onDeepLinkLaunched: () -> Unit,
    onShowOnboarding: () -> Unit,
) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is HomeEvent.DeepLinksLaunched -> onDeepLinkLaunched()
                is HomeEvent.ShowOnboarding -> onShowOnboarding()
            }
        }
    }
}
