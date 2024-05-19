package dev.koga.deeplinklauncher.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.koga.deeplinklauncher.AddFolderBottomSheet
import dev.koga.deeplinklauncher.SharedScreen
import dev.koga.deeplinklauncher.navigateToDeepLinkDetails
import dev.koga.deeplinklauncher.screen.component.HomeHorizontalPager
import dev.koga.deeplinklauncher.screen.component.HomeLaunchDeepLinkBottomSheetContent
import dev.koga.deeplinklauncher.screen.component.HomeTabRow
import dev.koga.deeplinklauncher.screen.component.HomeTopBar
import dev.koga.deeplinklauncher.screen.component.OnboardingBottomSheet
import dev.koga.deeplinklauncher.screen.state.HomeEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
object HomeScreen : Screen {
    private const val DELAY_TO_SCROLL_TO_THE_TOP = 350L

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.getNavigatorScreenModel<HomeScreenModel>()

        val uiState by screenModel.uiState.collectAsState()

        val settingsScreen = rememberScreen(SharedScreen.Settings)

        val scope = rememberCoroutineScope()

        val allDeepLinksListState = rememberLazyListState()
        val favoritesDeepLinksListState = rememberLazyListState()
        val pagerState = rememberPagerState(
            initialPage = HomeTabPage.HISTORY.ordinal,
            pageCount = {
                HomeTabPage.entries.size
            },
        )
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState()
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

        HomeEventsHandler(
            events = screenModel.events,
            onDeepLinkLaunched = {
                scope.launch {
                    delay(DELAY_TO_SCROLL_TO_THE_TOP)
                    allDeepLinksListState.animateScrollToItem(index = 0)
                    favoritesDeepLinksListState.animateScrollToItem(index = 0)
                }
            },
            onShowOnboarding = {
                showOnboardingBottomSheet = true
            }
        )

        Scaffold(
            topBar = {
                HomeTopBar(
                    scrollBehavior = scrollBehavior,
                    onSettingsScreen = { navigator.push(settingsScreen) },
                )
            },
            bottomBar = {
                HomeLaunchDeepLinkBottomSheetContent(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                    value = uiState.inputText,
                    onValueChange = screenModel::onDeepLinkTextChanged,
                    launch = screenModel::launchDeepLink,
                    errorMessage = uiState.errorMessage,
                )
            },
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
            ) {
                HomeTabRow(pagerState = pagerState)

                HomeHorizontalPager(
                    allDeepLinks = uiState.deepLinks,
                    favoriteDeepLinks = uiState.favorites,
                    folders = uiState.folders,
                    pagerState = pagerState,
                    scrollBehavior = scrollBehavior,
                    paddingBottom = 320.dp,
                    onDeepLinkClicked = { bottomSheetNavigator.navigateToDeepLinkDetails(it.id) },
                    onDeepLinkLaunch = screenModel::launchDeepLink,
                    onFolderClicked = {
                        val screen = ScreenRegistry.get(SharedScreen.FolderDetails(it.id))
                        navigator.push(screen)
                    },
                    onFolderAdd = { showAddFolderBottomSheet = true },
                    allDeepLinksListState = allDeepLinksListState,
                    favoritesDeepLinksListState = favoritesDeepLinksListState,
                )
            }
        }
    }
}

@Composable
fun HomeEventsHandler(
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
