package dev.koga.deeplinklauncher.deeplink.ui.folder.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
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
import compose.icons.TablerIcons
import compose.icons.tablericons.Check
import compose.icons.tablericons.Pencil
import compose.icons.tablericons.X
import dev.koga.deeplinklauncher.designsystem.DLLTextField
import dev.koga.deeplinklauncher.designsystem.button.DLLFilledIconButton
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.designsystem.theme.LocalDimensions

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

                                DLLFilledIconButton(
                                    onClick = {
                                        inEditMode = false
                                        onSave(inputValue)
                                    },
                                    modifier = Modifier.size(18.dp),
                                    enabled = editButtonEnabled,
                                    colors = IconButtonDefaults.filledIconButtonColors(
                                        contentColor = MaterialTheme.colorScheme.onPrimary,
                                        containerColor = MaterialTheme.colorScheme.primary,
                                    ),
                                ) {
                                    Icon(
                                        imageVector = TablerIcons.Check,
                                        contentDescription = "Save",
                                    )
                                }

                                Spacer(modifier = Modifier.width(dimensions.mediumLarge))

                                DLLIconButton(
                                    onClick = { inEditMode = false },
                                    modifier = Modifier.size(18.dp),
                                ) {
                                    Icon(
                                        imageVector = TablerIcons.X,
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

                    DLLIconButton(
                        onClick = { inEditMode = true },
                        modifier = Modifier.size(18.dp),
                    ) {
                        Icon(
                            imageVector = TablerIcons.Pencil,
                            contentDescription = "Edit",
                        )
                    }
                }
            }
        }
    }
}
