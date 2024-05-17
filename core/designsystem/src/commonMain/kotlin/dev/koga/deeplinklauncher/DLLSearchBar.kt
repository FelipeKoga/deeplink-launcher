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
        onSearch = onChanged,
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

//    BasicTextField(
//        value = value,
//        onValueChange = onChanged,
//        keyboardOptions = KeyboardOptions.Default.copy(
//            imeAction = ImeAction.Search,
//        ),
//        modifier = modifier
//            .fillMaxWidth()
//            .border(.4.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
//            .onFocusChanged {
//                isFocused = it.isFocused
//            },
//
//        textStyle = MaterialTheme.typography.bodyLarge.copy(
//            fontWeight = FontWeight.SemiBold,
//            color = MaterialTheme.colorScheme.secondary,
//        ),
//        cursorBrush = Brush.linearGradient(
//            listOf(MaterialTheme.colorScheme.onBackground, MaterialTheme.colorScheme.onBackground)
//        )
//    ) {
//        TextFieldDefaults.DecorationBox(
//            value = value,
//            innerTextField = it,
//            enabled = true,
//            singleLine = true,
//            visualTransformation = VisualTransformation.None,
//            interactionSource = MutableInteractionSource(),
//            colors = TextFieldDefaults.colors(
//                focusedContainerColor = Color.Transparent,
//                unfocusedContainerColor = Color.Transparent,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent,
//                errorIndicatorColor = Color.Transparent,
//            ),
//            shape = CircleShape,
//            placeholder = {
//                Text(
//                    text = hint,
//                    style = MaterialTheme.typography.bodyMedium.copy(
//                        fontWeight = FontWeight.SemiBold,
//                    ),
//                )
//            },
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Rounded.Search,
//                    contentDescription = "",
//                    tint = if (isFocused) {
//                        MaterialTheme.colorScheme.primary
//                    } else {
//                        LocalContentColor.current
//                    },
//                )
//            },
//            trailingIcon = {
//                AnimatedVisibility(
//                    visible = value.isNotEmpty(),
//                ) {
//                    Icon(
//                        imageVector = Icons.Rounded.Close,
//                        contentDescription = "",
//                        tint = if (isFocused) {
//                            MaterialTheme.colorScheme.primary
//                        } else {
//                            LocalContentColor.current
//                        },
//                        modifier = Modifier.clickable {
//                            isFocused = false
//                            focusManager.clearFocus()
//                            onChanged("")
//                        },
//                    )
//                }
//            },
//            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
//                top = 0.dp,
//                bottom = 0.dp,
//            ),
//        )
//    }
}
