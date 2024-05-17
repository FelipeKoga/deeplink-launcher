package dev.koga.deeplinklauncher

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction

val defaultTextFieldColors: TextFieldColors
    @Composable get() = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DLLTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    imeAction: ImeAction = ImeAction.Next,
    focusDirection: FocusDirection = FocusDirection.Down,
    colors: TextFieldColors = defaultTextFieldColors,
    onDone: () -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        modifier = modifier
            .fillMaxWidth()
            .clip(SearchBarDefaults.dockedShape),
        onValueChange = onValueChange,
        readOnly = readOnly,
        textStyle = textStyle,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        },
        colors = colors,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
        ),
        trailingIcon = trailingIcon,
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(focusDirection) },
            onDone = {
                focusManager.clearFocus()
                onDone()
            },
        ),
    )
}
