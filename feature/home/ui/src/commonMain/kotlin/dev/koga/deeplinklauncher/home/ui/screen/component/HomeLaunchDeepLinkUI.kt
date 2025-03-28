package dev.koga.deeplinklauncher.home.ui.screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ExternalLink
import compose.icons.tablericons.X
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.DLLTextField
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.designsystem.defaultTextFieldColors
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun HomeLaunchDeepLinkUI(
    modifier: Modifier = Modifier,
    value: String,
    errorMessage: String? = null,
    suggestions: ImmutableList<String>,
    onValueChange: (String) -> Unit,
    launch: () -> Unit,
    onSuggestionClicked: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isFocused by rememberSaveable { mutableStateOf(false) }

    val showSuggestions by remember(isFocused, suggestions) {
        derivedStateOf { isFocused && suggestions.isNotEmpty() }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DLLTextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (!isFocused) {
                            isFocused = it.isFocused
                        }
                    },
                value = value,
                onValueChange = onValueChange,
                label = "Type your deeplink here...",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Uri,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done,
                ),
                colors = defaultTextFieldColors.copy(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { launch() },
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                trailingIcon = {
                    AnimatedVisibility(
                        visible = isFocused,
                    ) {
                        IconButton(
                            onClick = {
                                isFocused = false
                                focusManager.clearFocus()
                                onValueChange("")
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.secondary,
                            ),
                            modifier = modifier,
                        ) {
                            Icon(
                                imageVector = TablerIcons.X,
                                contentDescription = "Clear",
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                },
            )

            DLLIconButton(
                modifier = Modifier.padding(start = 12.dp),
                onClick = launch,
                enabled = value.isNotBlank(),
            ) {
                Icon(
                    imageVector = TablerIcons.ExternalLink,
                    contentDescription = "Launch",
                )
            }
        }

        AnimatedVisibility(
            visible = errorMessage != null,
        ) {
            Text(
                text = errorMessage.orEmpty(),
                modifier = Modifier.padding(bottom = 4.dp),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }

        AnimatedVisibility(
            visible = showSuggestions,
        ) {
            LazyColumn(
                modifier = Modifier.animateContentSize(),
            ) {
                item {
                    DLLHorizontalDivider(thickness = .3.dp)
                }

                items(
                    items = suggestions,
                    key = { it },
                ) { suggestion ->
                    SuggestionListItem(
                        modifier = Modifier
                            .animateItem(fadeInSpec = null, fadeOutSpec = null)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                focusRequester.requestFocus()
                                onSuggestionClicked(suggestion)
                            },
                        suggestion = suggestion,
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestionListItem(modifier: Modifier = Modifier, suggestion: String) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(),
        exit = slideOutHorizontally(),
    ) {
        ListItem(
            modifier = modifier,
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
            ),
            headlineContent = {
                Text(
                    text = suggestion,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            },
        )
    }
}
