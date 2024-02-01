package dev.koga.deeplinklauncher

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.koga.deeplinklauncher.component.HomeHorizontalPager
import dev.koga.deeplinklauncher.component.HomeLaunchDeepLinkBottomSheetContent
import dev.koga.deeplinklauncher.component.HomeTabRow
import dev.koga.deeplinklauncher.component.HomeTopBar
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeScreenContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun HomeScreenContent() {
    val settingsScreen = rememberScreen(SharedScreen.Settings)

    val clipboardManager = LocalClipboardManager.current

    val navigator = LocalNavigator.currentOrThrow
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded,
    )
    val pagerState = rememberPagerState(
        initialPage = HomeTabPage.HISTORY.ordinal,
        pageCount = {
            HomeTabPage.entries.size
        },
    )

    val screenModel = navigator.getNavigatorScreenModel<HomeScreenModel>()
    val allDeepLinks by screenModel.deepLinks.collectAsState()
    val favoriteDeepLinks by screenModel.favoriteDeepLinks.collectAsState()
    val folders by screenModel.folders.collectAsState()
    val errorMessage by screenModel.errorMessage.collectAsState()
    val deepLinkText by screenModel.deepLinkText.collectAsState()

    val scrollBehavior = TopAppBarDefaults
        .exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var openFolderBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    if (openFolderBottomSheet) {
        AddFolderBottomSheet(
            onDismiss = {
                openFolderBottomSheet = false
            },
            onAdd = { name, description ->
                openFolderBottomSheet = false
                screenModel.addFolder(name, description)
            },
        )
    }

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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        sheetContent = {
            HomeLaunchDeepLinkBottomSheetContent(
                value = deepLinkText,
                onValueChange = screenModel::onDeepLinkTextChanged,
                launch = screenModel::launchDeepLink,
                errorMessage = errorMessage,
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
                allDeepLinks = allDeepLinks.toPersistentList(),
                favoriteDeepLinks = favoriteDeepLinks.toPersistentList(),
                folders = folders.toPersistentList(),
                pagerState = pagerState,
                scrollBehavior = scrollBehavior,
                paddingBottom = 320.dp,
                onDeepLinkClicked = {
                    val screen = ScreenRegistry.get(SharedScreen.DeepLinkDetails(it.id))
                    navigator.push(screen)
                },
                onDeepLinkLaunch = screenModel::launchDeepLink,
                onDeepLinkCopy = {
                    scope.launch {
                        clipboardManager.setText(AnnotatedString(it.link))
                        snackbarHostState.showSnackbar(
                            message = "Copied to clipboard",
                            actionLabel = "Dismiss",
                        )
                    }
                },
                onFolderClicked = {
                    val screen = ScreenRegistry.get(SharedScreen.FolderDetails(it.id))
                    navigator.push(screen)
                },
                onFolderAdd = {
                    openFolderBottomSheet = true
                },
            )
        }
    }
}
