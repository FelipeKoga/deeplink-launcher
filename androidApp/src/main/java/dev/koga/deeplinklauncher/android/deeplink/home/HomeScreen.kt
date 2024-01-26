package dev.koga.deeplinklauncher.android.deeplink.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.koga.deeplinklauncher.android.R
import dev.koga.deeplinklauncher.android.core.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.android.deeplink.detail.DeepLinkDetailsScreen
import dev.koga.deeplinklauncher.android.deeplink.home.component.DeepLinkItem
import dev.koga.deeplinklauncher.android.deeplink.home.component.DeepLinkLaunchBottomSheetContent
import dev.koga.deeplinklauncher.android.export.ExportScreen
import dev.koga.deeplinklauncher.android.folder.AddFolderBottomSheet
import dev.koga.deeplinklauncher.android.folder.FolderCard
import dev.koga.deeplinklauncher.android.folder.detail.FolderDetailsScreen
import dev.koga.deeplinklauncher.android.import.ImportScreen
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

    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded,
    )


    val pagerState = rememberPagerState(
        initialPage = HomeTabPage.HISTORY.ordinal,
        pageCount = {
            HomeTabPage.entries.size
        },
    )

    val navigator = LocalNavigator.currentOrThrow

    val screenModel = navigator.getNavigatorScreenModel<HomeScreenModel>()
    val snackbarHostState = remember { SnackbarHostState() }

    val deepLinks by screenModel.deepLinks.collectAsState()
    val favoriteDeepLinks by screenModel.favoriteDeepLinks.collectAsState()
    val errorMessage by screenModel.errorMessage.collectAsState()
    val deepLinkText by screenModel.deepLinkText.collectAsState()
    val folders by screenModel.folders.collectAsState()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var openFolderBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    var openImportExportOptionBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    val mainContentPaddingBottom = 320.dp

    if (openFolderBottomSheet) {
        AddFolderBottomSheet(
            onDismiss = {
                openFolderBottomSheet = false
            },
            onAdd = { name, description ->
                openFolderBottomSheet = false
                screenModel.addFolder(name, description)
            }
        )
    }

    BottomSheetScaffold(
        topBar = {
            DLLTopBar(
                scrollBehavior = scrollBehavior,
                title = "Deeplink Launcher",
                actions = {
                    FilledTonalIconButton(onClick = { openImportExportOptionBottomSheet = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_round_import_export_24),
                            contentDescription = "",
                            modifier = Modifier.size(18.dp),
                        )
                    }

                    DropdownMenu(
                        expanded = openImportExportOptionBottomSheet,
                        onDismissRequest = { openImportExportOptionBottomSheet = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Export") },
                            onClick = {
                                navigator.push(ExportScreen())
                                openImportExportOptionBottomSheet = false
                            },
                            leadingIcon = {
                                Icon(
                                    painterResource(id = R.drawable.ic_round_arrow_upward_24),
                                    contentDescription = null
                                )
                            })

                        DropdownMenuItem(
                            text = { Text("Import") },
                            onClick = {
                                navigator.push(ImportScreen())
                                openImportExportOptionBottomSheet = false
                            },
                            leadingIcon = {
                                Icon(
                                    painterResource(id = R.drawable.ic_round_arrow_downward_24),
                                    contentDescription = null
                                )
                            })
                    }
                }
            )
        },
        scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = bottomSheetState
        ),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        sheetContent = {
            DeepLinkLaunchBottomSheetContent(
                value = deepLinkText,
                onValueChange = screenModel::onDeepLinkTextChanged,
                launch = screenModel::launchDeepLink,
                errorMessage = errorMessage
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
        ) {

            TabRow(selectedTabIndex = pagerState.currentPage) {
                Tab(
                    selected = pagerState.currentPage == HomeTabPage.HISTORY.ordinal,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(HomeTabPage.HISTORY.ordinal)
                        }
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_round_history_24),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "History")
                        }
                    }
                )
                Tab(
                    selected = pagerState.currentPage == HomeTabPage.FAVORITES.ordinal,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(HomeTabPage.FAVORITES.ordinal)
                        }

                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Favorite,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Favorites")
                        }
                    }
                )
                Tab(
                    selected = pagerState.currentPage == HomeTabPage.FOLDERS.ordinal,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(HomeTabPage.FOLDERS.ordinal)
                        }
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_round_folder_24),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Folders")
                        }
                    }

                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    HomeTabPage.HISTORY.ordinal -> {
                        LazyColumn(
                            modifier = Modifier
                                .padding(contentPadding)
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            contentPadding = PaddingValues(
                                start = 12.dp,
                                end = 12.dp,
                                top = 12.dp,
                                bottom = mainContentPaddingBottom
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(deepLinks) { deepLink ->
                                DeepLinkItem(
                                    deepLink = deepLink,
                                    onClick = {
                                        navigator.push(DeepLinkDetailsScreen(it.id))
                                    },
                                    onLaunch = screenModel::launchDeepLink,
                                )
                            }
                        }
                    }

                    HomeTabPage.FAVORITES.ordinal -> {
                        LazyColumn(
                            modifier = Modifier
                                .padding(contentPadding)
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            contentPadding = PaddingValues(
                                start = 12.dp,
                                end = 12.dp,
                                top = 12.dp,
                                bottom = mainContentPaddingBottom
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(favoriteDeepLinks) { deepLink ->
                                DeepLinkItem(
                                    deepLink = deepLink,
                                    onClick = {
                                        navigator.push(DeepLinkDetailsScreen(it.id))
                                    },
                                    onLaunch = screenModel::launchDeepLink,
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
                                    bottom = mainContentPaddingBottom
                                ),
                                horizontalArrangement = Arrangement.spacedBy(24.dp),
                                verticalItemSpacing = 24.dp,
                                columns = StaggeredGridCells.Fixed(2)
                            ) {
                                item {
                                    OutlinedCard(
                                        onClick = {
                                            openFolderBottomSheet = true
                                        }, shape = RoundedCornerShape(24.dp),
                                        border = BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = .3f)
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(184.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Add,
                                                contentDescription = null,
                                                modifier = Modifier.size(24.dp),
                                                tint = MaterialTheme.colorScheme.onSurface
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Create new folder",
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }

                                }
                                items(folders.size) { index ->
                                    FolderCard(
                                        folder = folders[index],
                                        onClick = {
                                            navigator.push(FolderDetailsScreen(it.id))
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}