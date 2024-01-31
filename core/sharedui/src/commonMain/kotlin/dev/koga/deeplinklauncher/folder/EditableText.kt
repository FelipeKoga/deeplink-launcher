package dev.koga.deeplinklauncher.folder

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FilledTonalIconButton
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
    onSave: (String) -> Unit,
    inputLabel: String,
    editButtonEnabled: Boolean = true,
    textContent: @Composable () -> Unit,
) {
    val dimensions = LocalDimensions.current

    var inEditMode by rememberSaveable {
        mutableStateOf(false)
    }

    var inputValue by rememberSaveable(value) {
        mutableStateOf(value)
    }

    AnimatedContent(
        targetState = inEditMode,
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
                        value = inputValue,
                        onValueChange = {
                            inputValue = it
                        },
                        trailingIcon = {
                            Row {
                                Spacer(modifier = Modifier.width(dimensions.mediumLarge))

                                FilledTonalIconButton(
                                    onClick = {
                                        inEditMode = false
                                        onSave(inputValue)
                                    },
                                    enabled = editButtonEnabled,
                                    modifier = Modifier.size(18.dp),
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                    ),
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Done,
                                        contentDescription = "Save",
                                    )
                                }

                                Spacer(modifier = Modifier.width(dimensions.mediumLarge))

                                IconButton(
                                    onClick = {
                                        inEditMode = false
                                    },
                                    modifier = Modifier.size(18.dp),
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = "Close",
                                    )
                                }

                                Spacer(modifier = Modifier.width(dimensions.mediumLarge))
                            }
                        },
                    )
                }

                false -> {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        textContent()
                    }

                    Spacer(modifier = Modifier.width(dimensions.mediumLarge))

                    IconButton(
                        onClick = { inEditMode = true },
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
