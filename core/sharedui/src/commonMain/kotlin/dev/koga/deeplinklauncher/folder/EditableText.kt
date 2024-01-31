package dev.koga.deeplinklauncher.folder

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.DLLTextField
import dev.koga.deeplinklauncher.theme.LocalDimensions

@Composable
fun EditableText(
    modifier: Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    inputLabel: String,
    textContent: @Composable () -> Unit,
) {
    val dimensions = LocalDimensions.current

    var showEditDescriptionInput by rememberSaveable {
        mutableStateOf(false)
    }

    val folderDescription by rememberSaveable(value) {
        mutableStateOf(value)
    }

    AnimatedContent(
        targetState = showEditDescriptionInput,
        label = "",
    ) { target ->
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            when (target) {
                true -> {
                    DLLTextField(
                        label = inputLabel,
                        value = folderDescription,
                        onValueChange = onValueChanged,
                        trailingIcon = {
                            IconButton(
                                onClick = { showEditDescriptionInput = false },
                                modifier = Modifier.size(18.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Done,
                                    contentDescription = "Save",
                                )
                            }
                        },
                    )
                }

                false -> {
                    textContent()

                    Spacer(modifier = Modifier.width(dimensions.mediumLarge))

                    IconButton(
                        onClick = { showEditDescriptionInput = true },
                        modifier = Modifier.size(18.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.secondary,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit",
                        )
                    }
                }
            }
        }
    }
}