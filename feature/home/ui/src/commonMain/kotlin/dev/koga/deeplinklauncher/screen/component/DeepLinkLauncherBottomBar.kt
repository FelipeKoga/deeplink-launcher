package dev.koga.deeplinklauncher.screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.deeplinklauncher.DLLTextField
import dev.koga.resources.MR
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DeepLinkLauncherBottomBar(
    modifier: Modifier = Modifier,
    value: String,
    errorMessage: String? = null,
    suggestions: ImmutableList<String>,
    onValueChange: (String) -> Unit,
    launch: () -> Unit,
    onSuggestionClicked: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var isFocused by rememberSaveable { mutableStateOf(false) }

    val showSuggestions by remember(isFocused, suggestions) {
        derivedStateOf { isFocused && suggestions.isNotEmpty() }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(

                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .navigationBarsPadding()
            .padding(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DLLTextField(
                modifier = Modifier.weight(1f).onFocusChanged {
                    isFocused = it.isFocused
                },
                value = value,
                onValueChange = onValueChange,
                label = "Enter your deeplink here",
                imeAction = ImeAction.Search,
                keyboardActions = KeyboardActions(
                    onSearch = { launch() }
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "",
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = isFocused,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                isFocused = false
                                focusManager.clearFocus()
                                onValueChange("")
                            },
                        )
                    }
                }
            )

            AnimatedVisibility(
                visible = value.isNotBlank(),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                FilledIconButton(
                    onClick = launch, colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        painter = painterResource(MR.images.ic_launch_24dp),
                        contentDescription = "Launch",
                    )
                }
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
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LazyColumn {
                item {
                    Divider(color = MaterialTheme.colorScheme.onSurface, thickness = .2.dp)
                }

                items(suggestions) { suggestion ->
                    ListItem(
                        modifier = Modifier.animateItemPlacement().clickable {
                            onSuggestionClicked(suggestion)
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        ),
                        headlineContent = {
                            Text(
                                text = suggestion,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                ),
                            )
                        }
                    )
                }
            }
        }
    }
}
