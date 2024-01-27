package dev.koga.deeplinklauncher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.provider.DeepLinkClipboardProvider
import org.koin.compose.koinInject

@Composable
fun DeepLinkLaunchBottomSheetContent(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    launch: () -> Unit,
    errorMessage: String? = null,
) {

    val focusManager = LocalFocusManager.current

    val deepLinkClipboardProvider = koinInject<DeepLinkClipboardProvider>()
    val clipboardText by deepLinkClipboardProvider.clipboardText.collectAsState()

    LaunchedEffect(clipboardText) {
        onValueChange(clipboardText.orEmpty())
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
    ) {
        DLLTextField(
            label = "Enter your deeplink here",
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            trailingIcon = {
                AnimatedVisibility(visible = value.isNotEmpty()) {
                    IconButton(onClick = {
                        onValueChange("")
                        focusManager.clearFocus(force = true)
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "Clear",
                        )
                    }
                }
            },
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

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = launch,
            enabled = value.isNotBlank(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(.6f)
        ) {
            Text(text = "Launch")
        }
    }
}