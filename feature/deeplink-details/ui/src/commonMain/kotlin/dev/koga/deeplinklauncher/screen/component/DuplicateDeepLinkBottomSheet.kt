package dev.koga.deeplinklauncher.screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuplicateDeepLinkBottomSheet(
    currentLink: String,
    onConfirm: (newLink: String, copyAllFields: Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    errorMessage: String? = null,
) {

    var value by rememberSaveable { mutableStateOf(currentLink) }
    var copyAllFieldsChecked by rememberSaveable { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onDismissRequest
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "close"
                )
            }

            Text(
                text = "Duplicate DeepLink", style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            DLLTextField(
                label = "Enter your new deeplink",
                value = value,
                onValueChange = { value = it },
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Copy all fields",
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = copyAllFieldsChecked,
                    onCheckedChange = { copyAllFieldsChecked = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            FilledTonalButton(
                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.5f),
                onClick = { onConfirm(value, copyAllFieldsChecked) }
            ) {
                Text(
                    text = "Duplicate",
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}