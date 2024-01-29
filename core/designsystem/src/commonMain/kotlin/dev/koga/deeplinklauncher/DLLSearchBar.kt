package dev.koga.deeplinklauncher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun DLLSearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onChanged: (String) -> Unit,
    hint: String = "",
) {
    val focusManager = LocalFocusManager.current

    var isFocused by rememberSaveable {
        mutableStateOf(false)
    }

    TextField(
        value = value,
        onValueChange = onChanged,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        modifier = modifier
            .fillMaxWidth()
            .zIndex(1f)
            .shadow(
                elevation = 4.dp,
                shape = CircleShape,
            )
            .onFocusChanged {
                isFocused = it.isFocused
            },
        colors = TextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.onSurface,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "",
                tint = if (isFocused) MaterialTheme.colorScheme.primary
                else LocalContentColor.current
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = isFocused
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "",
                    tint = if (isFocused) MaterialTheme.colorScheme.primary
                    else LocalContentColor.current,
                    modifier = Modifier.clickable {
                        isFocused = false
                        focusManager.clearFocus()
                        onChanged("")
                    }
                )
            }
        },
        label = {
            Text(
                text = hint,
            )
        },
    )
}