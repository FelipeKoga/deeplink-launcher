package dev.koga.deeplinklauncher.designsystem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.X
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DLLSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography

    DockedSearchBar(
        modifier = modifier,
        colors = SearchBarDefaults.colors(
            containerColor = colors.surface.muted,
            inputFieldColors = TextFieldDefaults.colors(
                unfocusedContainerColor = colors.surface.muted,
                focusedContainerColor = colors.surface.muted,
            ),
        ),
        tonalElevation = 0.dp,
        query = query,
        placeholder = {
            Text(
                text = placeholder,
                style = typography.body.default.copy(color = colors.text.placeholder),
            )
        },
        onSearch = onQueryChange,
        active = false,
        content = {},
        onActiveChange = {},
        onQueryChange = onQueryChange,
        leadingIcon = {
            DLLIconButton(onClick = onClose) {
                Icon(
                    imageVector = TablerIcons.ArrowLeft,
                    contentDescription = "Back",
                )
            }
        },
        trailingIcon = {
            AnimatedVisibility(visible = query.isNotEmpty()) {
                DLLIconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = TablerIcons.X,
                        contentDescription = "Clear",
                    )
                }
            }
        },
    )
}
