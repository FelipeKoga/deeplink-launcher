package dev.koga.deeplinklauncher.screen.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.button.DLLIconButton
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
    var searchLayout by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedContent(searchLayout) { target ->
            when (target) {
                true -> HomeSearchBar(
                    modifier = Modifier
                        .padding(
                            top = 8.dp,
                            bottom = 4.dp,
                            start = 12.dp,
                            end = 12.dp,
                        )
                        .statusBarsPadding() ,
                    value = search,
                    onSearch = onSearch,
                    onClose = {
                        searchLayout = false
                        onSearch("")
                    },
                )

                false -> HomeTopBarImpl(
                    modifier = modifier,
                    scrollBehavior = scrollBehavior,
                    actions = {
                        DLLIconButton(
                            onClick = {
                                searchLayout = true
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
                                    dev.koga.resources.Res.drawable.ic_settings_24dp,
                                ),
                                contentDescription = "settings",
                            )
                        }
                    },
                )
            }
        }
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal expect fun HomeTopBarImpl(
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
)