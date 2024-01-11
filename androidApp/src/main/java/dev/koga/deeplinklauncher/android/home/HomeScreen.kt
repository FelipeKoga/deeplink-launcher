@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)

package dev.koga.deeplinklauncher.android.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.koga.deeplinklauncher.android.R
import dev.koga.deeplinklauncher.android.core.designsystem.DLLSearchBar
import dev.koga.deeplinklauncher.android.core.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.android.folder.FolderCard
import dev.koga.deeplinklauncher.android.home.component.DeepLinkDetailsBottomSheet
import dev.koga.deeplinklauncher.android.home.component.DeepLinkInputContent
import dev.koga.deeplinklauncher.android.settings.SettingsScreen
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import kotlinx.coroutines.launch

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeScreenContent()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent() {

    val scope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded,
        confirmValueChange = {
            if (it == SheetValue.PartiallyExpanded) {
                keyboardController?.hide()
            }

            true
        }
    )


    val pagerState = rememberPagerState(
        initialPage = HomeTabPage.HISTORY.ordinal,
        pageCount = {
            HomeTabPage.entries.size
        },
    )

    var mustOpenInputBottomSheetAfterDetailsDismiss by remember {
        mutableStateOf(false)
    }

    val navigator = LocalNavigator.currentOrThrow

    val screenModel = navigator.getNavigatorScreenModel<HomeScreenModel>()

    val deepLinks by screenModel.deepLinks.collectAsState()
    val favoriteDeepLinks by screenModel.favoriteDeepLinks.collectAsState()
    val errorMessage by screenModel.errorMessage.collectAsState()
    val deepLinkText by screenModel.deepLinkText.collectAsState()
    val selectedDeepLink by screenModel.selectedDeepLink.collectAsState()
    val searchText by screenModel.searchText.collectAsState()
    val folders by screenModel.folders.collectAsState()

    val mainContentPaddingBottom = 320.dp

    if (selectedDeepLink != null) {
        DeepLinkDetailsBottomSheet(
            deepLink = selectedDeepLink!!,
            onShare = screenModel::share,
            onDelete = screenModel::delete,
            onFavorite = screenModel::favorite,
            onDismiss = {
                screenModel.clearSelectedDeepLink()

                if (mustOpenInputBottomSheetAfterDetailsDismiss) {
                    mustOpenInputBottomSheetAfterDetailsDismiss = false
                    scope.launch {
                        bottomSheetState.expand()
                    }
                }
            }
        )
    }

    BottomSheetScaffold(
        topBar = {
            DLLTopBar(
                title = "Deeplink Launcher",
                actions = {
                    FilledTonalIconButton(onClick = {
//                        navigator.push(ImportExportScreen)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_round_import_export_24),
                            contentDescription = "",
                            modifier = Modifier.size(18.dp),
                        )
                    }


                    FilledTonalIconButton(onClick = {
                        navigator.push(SettingsScreen)
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = "Delete",
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            )
        },
        scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = bottomSheetState
        ),
        sheetContent = {
            DeepLinkInputContent(
                value = deepLinkText,
                onValueChange = screenModel::onDeepLinkTextChanged,
                launch = screenModel::launchDeepLink,
                errorMessage = errorMessage
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            DLLSearchBar(
                modifier = Modifier.padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 12.dp,
                    bottom = 24.dp
                ),
                value = searchText,
                onChanged = screenModel::onSearchTextChanged,
                hint = "Search for deeplinks"
            )

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
                                .fillMaxSize(),
                            contentPadding = PaddingValues(bottom = mainContentPaddingBottom)
                        ) {
                            items(deepLinks) { deepLink ->
                                DeepLinkItem(
                                    modifier = Modifier.animateItemPlacement(),
                                    deepLink = deepLink,
                                    onClick = screenModel::launchDeepLink,
                                    onLongClick = {
                                        screenModel.selectDeepLink(it)
                                        scope.launch {
                                            if (bottomSheetState.currentValue == SheetValue.Expanded) {
                                                bottomSheetState.partialExpand()
                                                mustOpenInputBottomSheetAfterDetailsDismiss = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }

                    HomeTabPage.FAVORITES.ordinal -> {
                        LazyColumn(
                            modifier = Modifier
                                .padding(contentPadding)
                                .fillMaxSize(),
                            contentPadding = PaddingValues(bottom = mainContentPaddingBottom)
                        ) {
                            items(favoriteDeepLinks) { deepLink ->
                                DeepLinkItem(
                                    modifier = Modifier.animateItemPlacement(),
                                    deepLink = deepLink,
                                    onClick = screenModel::launchDeepLink,
                                    onLongClick = {
                                        screenModel.selectDeepLink(it)
                                        scope.launch {
                                            if (bottomSheetState.currentValue == SheetValue.Expanded) {
                                                bottomSheetState.partialExpand()
                                                mustOpenInputBottomSheetAfterDetailsDismiss = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }

                    HomeTabPage.FOLDERS.ordinal -> {
                        LazyVerticalStaggeredGrid(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 24.dp,
                                end = 24.dp,
                                top = 24.dp,
                                bottom = mainContentPaddingBottom
                            ),
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalItemSpacing = 24.dp,
                            columns = StaggeredGridCells.Fixed(2)
                        ) {
                            items(folders.size) { index ->
                                FolderCard(
                                    folder = folders[index],
                                    onClick = {},
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}


@Composable
fun DeepLinkItem(
    modifier: Modifier = Modifier,
    deepLink: DeepLink,
    onClick: (DeepLink) -> Unit,
    onLongClick: (DeepLink) -> Unit
) {
    Column(
        modifier = modifier
            .animatedListItem(key = deepLink.id)
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick(deepLink) },
                onLongClick = { onLongClick(deepLink) }
            )
    ) {

        Column(
            Modifier
                .padding(vertical = 24.dp, horizontal = 12.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = deepLink.link,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            deepLink.folder?.let {
                Spacer(modifier = Modifier.height(8.dp))
                ElevatedAssistChip(onClick = { /*TODO*/ }, label = {
                    Text(text = it.name)
                })
            }
        }


        Divider(modifier = Modifier.background(MaterialTheme.colorScheme.onSurface.copy(0.1f)))
    }
}

@Preview(showBackground = true)
@Composable
fun DeepLinkItemPreview() {
    DeepLinkItem(deepLink = DeepLink(
        id = "123",
        link = "https://www.google.com",
        name = null,
        description = null,
        createdAt = 4849,
        isFavorite = false,
        folder = null
    ), onClick = {}, onLongClick = {}
    )
}

fun <T> Modifier.animatedListItem(key: T): Modifier = composed {

    val animatedProgress = remember(key) {
        Animatable(initialValue = 0.85f)
    }

    LaunchedEffect(key1 = key) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        )
    }

    this.graphicsLayer(
        scaleX = animatedProgress.value,
        scaleY = animatedProgress.value
    )
}
