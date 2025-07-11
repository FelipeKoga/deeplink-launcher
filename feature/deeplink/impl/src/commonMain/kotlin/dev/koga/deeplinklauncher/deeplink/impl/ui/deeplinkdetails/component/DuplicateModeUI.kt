package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DuplicateAction
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.DLLTextField
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton

@Composable
internal fun DuplicateModeUI(
    uiState: DeepLinkDetailsUiState.Duplicate,
    onAction: (DuplicateAction) -> Unit,
) {
    var newLink by rememberSaveable { mutableStateOf(uiState.deepLink.link) }
    var copyAllFields by rememberSaveable { mutableStateOf(true) }

    Column {
        TopBar(onBack = { onAction(DuplicateAction.Back) })

        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
            DLLTextField(
                label = "Enter new deeplink",
                value = newLink,
                onValueChange = { newLink = it },
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
            )

            AnimatedVisibility(
                visible = uiState.errorMessage != null,
            ) {
                Text(
                    text = uiState.errorMessage.orEmpty(),
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                    )

                    Text(
                        text = "All fields will be copied to the new deeplink",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                    )
                }

                Switch(
                    checked = copyAllFields,
                    onCheckedChange = { copyAllFields = it },
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        DLLHorizontalDivider()

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.5f)
                .padding(24.dp),
            onClick = { onAction(DuplicateAction.Duplicate(newLink, copyAllFields)) },
        ) {
            Text(
                text = "Duplicate",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}

@Composable
internal fun TopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DLLIconButton(
            onClick = onBack,
        ) {
            Icon(
                imageVector = TablerIcons.ArrowLeft,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
