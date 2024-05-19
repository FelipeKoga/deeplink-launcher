package dev.koga.deeplinklauncher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DLLSearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    hint: String = "",
) {
    val focusManager = LocalFocusManager.current

    var isFocused by rememberSaveable {
        mutableStateOf(false)
    }

    DockedSearchBar(
        modifier = modifier,
        query = value,
        onQueryChange = onChanged,
        onSearch = onSearch,
        active = false,
        onActiveChange = {},
        content = {},
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "",
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = value.isNotEmpty(),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Clear,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        isFocused = false
                        focusManager.clearFocus()
                        onChanged("")
                    },
                )
            }
        },
    )
}
