package dev.koga.deeplinklauncher.android.deeplink.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.android.R
import dev.koga.deeplinklauncher.util.isUriValid

@Composable
fun DeepLinkLaunchBottomSheetContent(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    launch: () -> Unit,
    errorMessage: String? = null,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
    ) {
        TextField(
            value = value,
            modifier = Modifier
                .defaultMinSize(minHeight = 80.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            onValueChange = onValueChange,
            placeholder = {
                Text(text = "Enter your deeplink here")
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )

        AnimatedVisibility(
            visible = errorMessage != null,
        ) {
            Text(
                text = errorMessage.orEmpty(),
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = launch,
            enabled = value.isNotBlank(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(.6f)
        ) {
            Text(text = "Launch")
        }

        LaunchClipboardDeepLinkUI(
            currentText = value,
            launch = { clipboardText ->
                onValueChange(clipboardText)
                launch()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchClipboardDeepLinkUI(
    currentText: String,
    launch: (String) -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current
    var clipboardDeepLinkUri by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    var clipboardDismissed by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(clipboardManager.getText()) {
        if (clipboardManager.getText() == null) return@LaunchedEffect

        val clipboardText = clipboardManager.getText().toString()

        if (currentText == clipboardText) return@LaunchedEffect

        if (clipboardDeepLinkUri == clipboardText) return@LaunchedEffect

        clipboardDeepLinkUri = if (clipboardText.isUriValid()) {
            clipboardDismissed = false
            clipboardText
        } else {
            null
        }
    }

    AnimatedVisibility(
        visible = clipboardDeepLinkUri != null && !clipboardDismissed,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column {
            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider()

            SwipeToDismissBox(
                state = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.StartToEnd) {
                            clipboardDismissed = true
                        }

                        true
                    }
                ),
                backgroundContent = {},
            ) {
                Column {

                    Spacer(modifier = Modifier.height(12.dp))

                    val text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal
                            ),
                        ) {
                            append("Clipboard deeplink: ")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                            ),
                        ) {
                            append(clipboardDeepLinkUri.orEmpty())
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Icon(
                            painterResource(id = R.drawable.ic_round_content_copy_24),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = text,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        FilledTonalIconButton(onClick = {
                            launch(clipboardDeepLinkUri.orEmpty())
                            clipboardDeepLinkUri = null
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_round_launch_24),
                                contentDescription = "Launch",
                            )
                        }
                    }
                }
            }
        }
    }
}