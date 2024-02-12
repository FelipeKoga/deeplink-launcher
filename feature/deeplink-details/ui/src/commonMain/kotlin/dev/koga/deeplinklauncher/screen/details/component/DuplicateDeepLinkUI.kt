package dev.koga.deeplinklauncher.screen.details.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.DLLTextField
import dev.koga.deeplinklauncher.model.DeepLink


@Composable
fun DuplicateDeepLinkUI(
    deepLink: DeepLink,
    errorMessage: String? = null,
    onDuplicate: (newLink: String, copyAllFields: Boolean) -> Unit,
    onBack: () -> Unit
) {

    var newLink by rememberSaveable { mutableStateOf(deepLink.link) }
    var copyAllFields by rememberSaveable { mutableStateOf(true) }

    Column {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "Duplicate DeepLink",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            DLLTextField(
                label = "Enter your new deeplink",
                value = newLink,
                onValueChange = { newLink = it },
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                imeAction = ImeAction.Done,
                textStyle = LocalTextStyle.current.copy(
                    fontWeight = FontWeight.SemiBold,
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
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        text = "Copy all fields",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Text(
                        text = "All fields will be copied to the new deeplink",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Switch(
                    checked = copyAllFields,
                    onCheckedChange = { copyAllFields = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(12.dp))

            FilledTonalButton(
                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.5f),
                onClick = { onDuplicate(newLink, copyAllFields) }
            ) {
                Text(
                    text = "Duplicate",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.navigationBarsPadding())
    }

}