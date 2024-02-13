package dev.koga.deeplinklauncher.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import dev.koga.deeplinklauncher.screen.state.HomeEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
object HomeScreen : Screen {

    @Composable
    override fun Content() {
        val settingsScreen = rememberScreen(SharedScreen.Settings)

        val scope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded,
        )

        val pagerState = rememberPagerState(
            initialPage = HomeTabPage.HISTORY.ordinal,
            pageCount = {
                HomeTabPage.entries.size
            },
        )

        val deepLinksLazyListState = rememberLazyListState()

        val screenModel = navigator.getNavigatorScreenModel<HomeScreenModel>()
        val uiState by screenModel.uiState.collectAsState()

        val scrollBehavior = TopAppBarDefaults
            .exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

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

        LaunchedEffect(Unit) {
            snapshotFlow { bottomSheetNavigator.isVisible }
                .collect { isVisible -> if (isVisible) bottomSheetState.partialExpand() }
        }

        HomeEventsHandler(
            events = screenModel.events,
            onDeepLinkLaunched = {
                scope.launch {
                    deepLinksLazyListState.animateScrollToItem(index = 0)
                }
            }
        )

        BottomSheetScaffold(
            topBar = {
                HomeTopBar(
                    scrollBehavior = scrollBehavior,
                    onSettingsScreen = { navigator.push(settingsScreen) },
                )
            },
            scaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = bottomSheetState,
            ),
            sheetContainerColor = MaterialTheme.colorScheme.surface,
            sheetContent = {
                HomeLaunchDeepLinkBottomSheetContent(
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
                    deepLinksListState = deepLinksLazyListState
                )
            }
        }
    }
}

@Composable
fun HomeEventsHandler(
    events: Flow<HomeEvent>,
    onDeepLinkLaunched: () -> Unit,
) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is HomeEvent.DeepLinksLaunched -> onDeepLinkLaunched()
            }
        }
    }
}
