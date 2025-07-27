package dev.koga.deeplinklauncher.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import compose.icons.tablericons.ArrowUp
import compose.icons.tablericons.ExternalLink
import compose.icons.tablericons.X
import dev.koga.deeplinklauncher.deeplink.api.model.Suggestion
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.DLLTextField
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.designsystem.defaultTextFieldColors
import dev.koga.deeplinklauncher.home.state.DeepLinkInputState
import kotlinx.coroutines.delay

@Composable
internal fun HomeBottomBarUI(
    modifier: Modifier = Modifier,
    state: DeepLinkInputState,
    onValueChange: (String) -> Unit,
    launch: () -> Unit,
    onSuggestionClicked: (Suggestion) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isFocused by rememberSaveable { mutableStateOf(false) }

    val showSuggestions by remember(isFocused, state.suggestions) {
        derivedStateOf { isFocused && state.suggestions.isNotEmpty() }
    }

    var visible by remember { mutableStateOf(false) }

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
                value = state.text,
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
                enabled = state.text.isNotBlank(),
            ) {
                Icon(
                    imageVector = TablerIcons.ExternalLink,
                    contentDescription = "Launch",
                )
            }
        }

        AnimatedVisibility(
            visible = state.errorMessage != null,
        ) {
            Text(
                text = state.errorMessage.orEmpty(),
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
                    items = state.suggestions,
                    key = { it.text },
                ) { suggestion ->
                    SuggestionListItem(
                        modifier = Modifier
                            .animateItem()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                focusRequester.requestFocus()
                                onSuggestionClicked(suggestion)
                            },
                        suggestion = suggestion,
                        visible = visible,
                        onVisibleChanged = { visible = it },
                    )
                }
            }
        }
    }
}

@Composable
internal fun SuggestionListItem(
    modifier: Modifier = Modifier,
    visible: Boolean,
    suggestion: Suggestion,
    animationDelay: Long = 0,
    onVisibleChanged: (Boolean) -> Unit = {},
) {
    LaunchedEffect(Unit) {
        delay(animationDelay)
        onVisibleChanged(true)
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(),
        exit = slideOutHorizontally(),
    ) {
        Row(
            modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                if (suggestion is Suggestion.Clipboard) {
                    Text(
                        text = "Deeplink from clipboard",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary,
                        ),
                    )
                }
                Text(
                    text = suggestion.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }

            Icon(
                imageVector = TablerIcons.ArrowUp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}
