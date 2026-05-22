package dev.koga.deeplinklauncher.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Search
import compose.icons.tablericons.Settings
import dev.koga.deeplinklauncher.designsystem.DLLSearchBar
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import kotlinx.coroutines.delay

@Composable
expect fun HomeTopBarTitle(modifier: Modifier = Modifier)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeTopBar(
    modifier: Modifier = Modifier,
    search: String,
    onSettingsScreen: () -> Unit,
    onSearch: (String) -> Unit,
    pagerState: PagerState,
) {
    val focusRequester = remember { FocusRequester() }
    var isSearching by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(isSearching) {
        if (isSearching) {
            delay(150L)
            focusRequester.requestFocus()
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Column {
            DLLTopBar(
                title = { HomeTopBarTitle() },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                actions = {
                    DLLIconButton(
                        onClick = {
                            isSearching = true
                        },
                    ) {
                        Icon(
                            imageVector = TablerIcons.Search,
                            contentDescription = "settings",
                        )
                    }

                    DLLIconButton(
                        onClick = onSettingsScreen,
                    ) {
                        Icon(
                            imageVector = TablerIcons.Settings,
                            contentDescription = "settings",
                        )
                    }
                },
            )

            HomeTabRow(pagerState = pagerState)
        }

        AnimatedVisibility(
            isSearching,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = 500),
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
            ),
        ) {
            DLLSearchBar(
                query = search,
                onQueryChange = onSearch,
                onClose = {
                    isSearching = false
                    focusRequester.freeFocus()
                    onSearch("")
                },
                placeholder = "Search for deeplinks and folders",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 4.dp,
                        start = 12.dp,
                        end = 12.dp,
                    )
                    .statusBarsPadding()
                    .focusRequester(focusRequester),
            )
        }
    }
}
