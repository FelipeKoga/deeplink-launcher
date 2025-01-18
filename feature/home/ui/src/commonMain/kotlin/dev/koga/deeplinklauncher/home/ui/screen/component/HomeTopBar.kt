package dev.koga.deeplinklauncher.home.ui.screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.resources.Res
import dev.koga.resources.ic_settings_24dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeTopBar(
    modifier: Modifier = Modifier,
    search: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onSettingsScreen: () -> Unit,
    onSearch: (String) -> Unit,
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

    Box(modifier = Modifier.fillMaxWidth()) {
        DLLTopBar(
            modifier = modifier,
            scrollBehavior = scrollBehavior,
            title = { HomeTopBarTitle() },
            actions = {
                DLLIconButton(
                    onClick = {
                        isSearching = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "settings",
                    )
                }

                DLLIconButton(
                    onClick = onSettingsScreen,
                ) {
                    Icon(
                        painter = painterResource(
                            Res.drawable.ic_settings_24dp,
                        ),
                        contentDescription = "settings",
                    )
                }
            },
        )

        AnimatedVisibility(
            isSearching,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // Start from the right
                animationSpec = tween(durationMillis = 500),
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
            ),
        ) {
            HomeSearchBar(
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
                value = search,
                onSearch = onSearch,
                onClose = {
                    isSearching = false
                    focusRequester.freeFocus()
                    onSearch("")
                },
            )
        }
    }
}

@Composable
expect fun HomeTopBarTitle(modifier: Modifier = Modifier)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeSearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onSearch: (String) -> Unit,
    onClose: () -> Unit,
) {
    DockedSearchBar(
        modifier = modifier,
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            inputFieldColors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ),
        tonalElevation = 0.dp,
        query = value,
        placeholder = {
            Text(
                text = "Search for deeplinks and folders",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                ),
            )
        },
        onSearch = onSearch,
        active = false,
        content = {},
        onActiveChange = {},
        onQueryChange = onSearch,
        leadingIcon = {
            DLLIconButton(
                onClick = onClose,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        trailingIcon = {
            AnimatedVisibility(visible = value.isNotEmpty()) {
                DLLIconButton(
                    onClick = { onSearch("") },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "Clear",
                    )
                }
            }
        },
    )
}
