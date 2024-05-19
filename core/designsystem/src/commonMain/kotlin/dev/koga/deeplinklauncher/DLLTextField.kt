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
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue

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
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardActions: KeyboardActions = KeyboardActions(
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    ),
    imeAction: ImeAction = ImeAction.Next,
    colors: TextFieldColors = defaultTextFieldColors,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val textFieldValue = remember(value) {
        TextFieldValue(
            text = value,
            selection = TextRange(value.length)
        )
    }

    TextField(
        value = textFieldValue,
        modifier = modifier
            .fillMaxWidth()
            .clip(SearchBarDefaults.dockedShape),
        onValueChange = {
            onValueChange(it.text)
        },
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
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardActions = keyboardActions,
//        KeyboardActions(
//            onNext = { focusManager.moveFocus(focusDirection) },
//            onDone = {
//                focusManager.clearFocus()
//                onDone()
//            },
//        ),
    )
}
