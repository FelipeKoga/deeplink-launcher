package dev.koga.deeplinklauncher.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.LaunchDeepLink
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainContent() {

    val context = LocalContext.current

    val launchDeepLink = remember {
        LaunchDeepLink(context)
    }

    var deepLinkText by rememberSaveable {
        mutableStateOf("")
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded
        )
    )

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = HomeTabPage.HISTORY.ordinal,
        pageCount = {
            HomeTabPage.entries.size
        },
    )

    LaunchedEffect(pagerState.currentPage) {

    }

    BottomSheetScaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "Deeplink Launcher")
            })
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                OutlinedTextField(
                    value = deepLinkText,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 80.dp)
                        .fillMaxWidth(),
                    onValueChange = {
                        deepLinkText = it
                    },
                    placeholder = {
                        Text(text = "Insert the deeplink here...")
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        launchDeepLink.launch(deepLinkText)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Launch")
                }

            }
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
                    text = { Text(text = "History") }
                )
                Tab(
                    selected = pagerState.currentPage == HomeTabPage.FAVORITES.ordinal,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(HomeTabPage.FAVORITES.ordinal)
                        }

                    },
                    text = { Text(text = "Favorites") }
                )
                Tab(
                    selected = pagerState.currentPage == HomeTabPage.FOLDERS.ordinal,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(HomeTabPage.FOLDERS.ordinal)
                        }
                    },
                    text = { Text(text = "Folders") }

                )
            }

            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    HomeTabPage.HISTORY.ordinal -> {
                        LazyColumn(
                            modifier = Modifier
                                .padding(contentPadding)
                                .fillMaxSize(),
                            contentPadding = PaddingValues(24.dp)
                        ) {
                            items(deepLinkSamples) {
                                DeepLinkItem(it)
                            }
                        }
                    }

                    HomeTabPage.FAVORITES.ordinal -> {
                        LazyColumn(
                            modifier = Modifier
                                .padding(contentPadding)
                                .fillMaxSize(),
                            contentPadding = PaddingValues(24.dp)
                        ) {
                            items(deepLinkSamples) {
                                DeepLinkItem(it)
                            }
                        }
                    }

                    HomeTabPage.FOLDERS.ordinal -> {
                        LazyColumn(
                            modifier = Modifier
                                .padding(contentPadding)
                                .fillMaxSize(),
                            contentPadding = PaddingValues(24.dp)
                        ) {
                            items(deepLinkSamples) {
                                DeepLinkItem(it)
                            }
                        }
                    }
                }
            }
        }

//        LazyColumn(
//            modifier = Modifier
//                .padding(contentPadding)
//                .fillMaxSize(),
//            contentPadding = PaddingValues(24.dp)
//        ) {
//            item {
//                Text(text = "History", style = MaterialTheme.typography.labelLarge)
//            }
//
//            items(deepLinkSamples) {
//                DeepLinkItem(it)
//            }
//        }
    }

}


enum class HomeTabPage {
    HISTORY,
    FAVORITES,
    FOLDERS
}

@Composable
fun DeepLinkItem(deepLink: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = deepLink)
    }
}

private val deepLinkSamples = listOf(
    "https://www.google.com",
    "https://www.google.com/search?q=android",
    "https://www.google.com/search?q=android&tbm=isch",
    "https://www.google.com/search?q=android&tbm=isch&hl=ja",
    "https://www.google.com/search?q=android&tbm=isch&hl=ja&safe=active",
    "https://www.google.com/se"
)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        MainContent()
    }
}
