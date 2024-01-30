package dev.koga.deeplinklauncher.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.FilledTonalIconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.deeplinklauncher.DLLTextField
import dev.koga.deeplinklauncher.provider.DeepLinkClipboardProvider
import dev.koga.resources.MR
import org.koin.compose.koinInject

@Composable
internal fun HomeLaunchDeepLinkBottomSheetContent(
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
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            DLLTextField(
                label = "Enter your deeplink here",
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.clip(RoundedCornerShape(12.dp)).weight(1f),
                imeAction = ImeAction.Done,
                onDone = launch,
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

            Spacer(modifier = Modifier.width(8.dp))

            AnimatedVisibility(
                visible = value.isNotEmpty(),
            ) {
                FilledTonalIconButton(
                    onClick = launch,
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
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}
