package dev.koga.deeplinklauncher.screen

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
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.koga.deeplinklauncher.AddFolderBottomSheet
import dev.koga.deeplinklauncher.SharedScreen
import dev.koga.deeplinklauncher.deeplink.DeepLinkActionsBottomSheet
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.navigateToDeepLinkDetails
import dev.koga.deeplinklauncher.screen.component.HomeHorizontalPager
import dev.koga.deeplinklauncher.screen.component.HomeLaunchDeepLinkBottomSheetContent
import dev.koga.deeplinklauncher.screen.component.HomeTabRow
import dev.koga.deeplinklauncher.screen.component.HomeTopBar
import kotlinx.coroutines.launch

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeScreenContent()
    }
}

private sealed interface BottomSheetAction {
    data class DeepLinkActions(val id: String) : BottomSheetAction
    data object AddFolder : BottomSheetAction
    data object Idle : BottomSheetAction
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun HomeScreenContent() {
    val settingsScreen = rememberScreen(SharedScreen.Settings)

    val navigator = LocalNavigator.currentOrThrow
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
    val uiState by screenModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults
        .exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var bottomSheetAction by remember {
        mutableStateOf<BottomSheetAction>(BottomSheetAction.Idle)
    }

    when (bottomSheetAction) {
        BottomSheetAction.AddFolder -> AddFolderBottomSheet(
            onDismiss = { bottomSheetAction = BottomSheetAction.Idle },
            onAdd = { name, description ->
                bottomSheetAction = BottomSheetAction.Idle
                screenModel.addFolder(name, description)
            },
        )

        is BottomSheetAction.DeepLinkActions -> {
            val deepLink = remember(uiState.deepLinks) {
                val deepLinkId = (bottomSheetAction as BottomSheetAction.DeepLinkActions).id
                uiState.deepLinks.firstOrNull { it.id == deepLinkId }
            } ?: return

            DeepLinkActionsBottomSheet(
                deepLink = deepLink,
                onDismiss = { bottomSheetAction = BottomSheetAction.Idle },
                onShare = { screenModel.share(deepLink) },
                onFavorite = { screenModel.toggleFavorite(deepLink) },
                onLaunch = { screenModel.launchDeepLink(deepLink) },
                onDetails = {
                    bottomSheetAction = BottomSheetAction.Idle
                    navigator.navigateToDeepLinkDetails(deepLink.id)
                },
            )
        }

        BottomSheetAction.Idle -> Unit
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
                onDeepLinkClicked = { navigator.navigateToDeepLinkDetails(it.id) },
                onDeepLinkLaunch = screenModel::launchDeepLink,
                onFolderClicked = {
                    val screen = ScreenRegistry.get(SharedScreen.FolderDetails(it.id))
                    navigator.push(screen)
                },
                onFolderAdd = { bottomSheetAction = BottomSheetAction.AddFolder },
                onOpenDeepLinkActions = {
                    bottomSheetAction = BottomSheetAction.DeepLinkActions(it.id)
                },
            )
        }
    }
}