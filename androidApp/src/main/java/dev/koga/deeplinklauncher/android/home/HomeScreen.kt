package dev.koga.deeplinklauncher.android.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.LaunchDeepLink
import dev.koga.deeplinklauncher.LaunchDeepLinkResult
import dev.koga.deeplinklauncher.android.R
import dev.koga.deeplinklauncher.android.home.component.DeepLinkDetailsBottomSheet
import dev.koga.deeplinklauncher.android.home.component.DeepLinkInputContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current

    val launchDeepLink = remember {
        LaunchDeepLink(context)
    }

    var deepLinkText by rememberSaveable {
        mutableStateOf("")
    }

    var result by remember {
        mutableStateOf<LaunchDeepLinkResult?>(null)
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded,
        )
    )

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = HomeTabPage.HISTORY.ordinal,
        pageCount = {
            HomeTabPage.entries.size
        },
    )

    var deepLinkDetails by remember {
        mutableStateOf<String?>(null)
    }

    var mustOpenInputBottomSheetAfterDetailsDismiss by remember {
        mutableStateOf(false)
    }

    if (deepLinkDetails != null) {
        DeepLinkDetailsBottomSheet(deepLink = deepLinkDetails!!) {
            deepLinkDetails = null

            if (mustOpenInputBottomSheetAfterDetailsDismiss) {
                mustOpenInputBottomSheetAfterDetailsDismiss = false
                scope.launch {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
            }
        }
    }


    BottomSheetScaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Deeplink Launcher",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    FilledTonalIconButton(onClick = {
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_round_import_export_24),
                            contentDescription = "",
                            modifier = Modifier.size(18.dp),
                        )
                    }


                    FilledTonalIconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = "Delete",
                            modifier = Modifier.size(18.dp),
                        )
                    }

                })
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            DeepLinkInputContent(
                value = deepLinkText,
                onValueChange = {
                    deepLinkText = it
                },
                launch = {
                    result = launchDeepLink.launch(deepLinkText)
                },
                errorMessage = (result as? LaunchDeepLinkResult.Failure)?.throwable?.message
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
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

            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                when (page) {
                    HomeTabPage.HISTORY.ordinal -> {
                        LazyColumn(
                            modifier = Modifier
                                .padding(contentPadding)
                                .fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 240.dp)
                        ) {
                            items(deepLinkSamples) {
                                DeepLinkItem(
                                    it,
                                    onClick = {
                                        launchDeepLink.launch(it)
                                    },
                                    onLongClick = {
                                        deepLinkDetails = it
                                        scope.launch {
                                            if (bottomSheetScaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) {
                                                bottomSheetScaffoldState.bottomSheetState.partialExpand()
                                                mustOpenInputBottomSheetAfterDetailsDismiss = true
                                            }
                                        }
                                    })
                            }
                        }
                    }

                    HomeTabPage.FAVORITES.ordinal -> {

                    }

                    HomeTabPage.FOLDERS.ordinal -> {

                    }
                }
            }
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeepLinkItem(deepLink: String, onClick: (String) -> Unit, onLongClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick(deepLink) },
                onLongClick = { onLongClick(deepLink) }
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = deepLink,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = "Delete",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }


        Divider(modifier = Modifier.background(MaterialTheme.colorScheme.onSurface.copy(0.1f)))
    }
}

private val deepLinkSamples = listOf<String>(
    "https://www.google.com.br",
    "https://www.google.com.br",

    )

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}