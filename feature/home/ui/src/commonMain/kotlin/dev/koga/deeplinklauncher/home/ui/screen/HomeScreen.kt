package dev.koga.deeplinklauncher.home.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import dev.koga.deeplinklauncher.LocalRootNavigator
import dev.koga.deeplinklauncher.SharedScreen
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component.AddFolderBottomSheet
import dev.koga.deeplinklauncher.home.ui.screen.component.HomeHorizontalPager
import dev.koga.deeplinklauncher.home.ui.screen.component.HomeLaunchDeepLinkUI
import dev.koga.deeplinklauncher.home.ui.screen.component.HomeTabRow
import dev.koga.deeplinklauncher.home.ui.screen.component.HomeTopBar
import dev.koga.deeplinklauncher.home.ui.screen.component.OnboardingBottomSheet
import dev.koga.deeplinklauncher.home.ui.screen.state.HomeEvent
import dev.koga.deeplinklauncher.navigateToDeepLinkDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val navigator = LocalRootNavigator.current
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.getNavigatorScreenModel<HomeScreenModel>()
        val uiState by screenModel.uiState.collectAsState()

        val historyListState = rememberLazyGridState()
        val favoritesListState = rememberLazyGridState()
        val pagerState = rememberPagerState(
            initialPage = HomeTabPage.HISTORY.ordinal,
            pageCount = { HomeTabPage.entries.size },
        )
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState(),
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
                    scrollBehavior = scrollBehavior,
                    onSettingsScreen = {
                        val settingsScreen = ScreenRegistry.get(SharedScreen.Settings)
                        navigator.push(settingsScreen)
                    },
                    onSearch = screenModel::onSearch,
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                HomeLaunchDeepLinkUI(
                    modifier = Modifier.navigationBarsPadding(),
                    value = uiState.deepLinkInput,
                    onValueChange = screenModel::onDeepLinkTextChanged,
                    suggestions = uiState.suggestions,
                    launch = screenModel::launchDeepLink,
                    errorMessage = uiState.errorMessage,
                    onSuggestionClicked = { screenModel.onDeepLinkTextChanged(it) },
                )
            },
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                HomeTabRow(pagerState = pagerState)

                HomeHorizontalPager(
                    allDeepLinks = uiState.deepLinks,
                    favoriteDeepLinks = uiState.favorites,
                    folders = uiState.folders,
                    pagerState = pagerState,
                    scrollBehavior = scrollBehavior,
                    onDeepLinkClicked = { bottomSheetNavigator.navigateToDeepLinkDetails(it.id) },
                    onDeepLinkLaunch = screenModel::launchDeepLink,
                    onFolderClicked = {
                        val screen = ScreenRegistry.get(SharedScreen.FolderDetails(it.id))
                        navigator.push(screen)
                    },
                    onFolderAdd = { showAddFolderBottomSheet = true },
                    historyListState = historyListState,
                    favoritesListState = favoritesListState,
                )
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
