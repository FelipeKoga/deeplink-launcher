package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.designsystem.DLLTextField

@Composable
fun EditModeUI(
    modifier: Modifier,
    uiState: DeepLinkDetailsUiState,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onLinkChanged: (String) -> Unit,
    onAddFolder: (String, String) -> Unit,
    onToggleFolder: (Folder) -> Unit,
) {
    val deepLink = uiState.deepLink

    var showAddFolderBottomSheet by remember {
        mutableStateOf(false)
    }

    if (showAddFolderBottomSheet) {
        AddFolderBottomSheet(
            onDismiss = { showAddFolderBottomSheet = false },
            onAdd = { name, description ->
                onAddFolder(name, description)
                showAddFolderBottomSheet = false
            },
        )
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            DeepLinkDetailsTextField(
                text = deepLink.name.orEmpty(),
                onTextChange = onNameChanged,
                label = "Name",
            )

            Spacer(modifier = Modifier.height(12.dp))

            DeepLinkDetailsTextField(
                text = deepLink.description.orEmpty(),
                onTextChange = onDescriptionChanged,
                label = "Description",
            )

            Spacer(modifier = Modifier.height(12.dp))

            DeepLinkDetailsTextField(
                text = deepLink.link,
                onTextChange = onLinkChanged,
                label = "Link",
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = uiState.deepLinkErrorMessage != null,
            ) {
                Text(
                    text = uiState.deepLinkErrorMessage.orEmpty(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            item {
                AssistChip(
                    shape = CircleShape,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Add folder",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    ),
                    border = null,
                    onClick = { showAddFolderBottomSheet = true }
                )
            }

            items(uiState.folders) { folder ->
                val selected = uiState.deepLink.folder?.id == folder.id

                FilterChip(
                    selected = selected,
                    onClick = {
                        onToggleFolder(folder)
                    },
                    label = {
                        Text(
                            text = folder.name,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )

                        )
                    },
                    shape = CircleShape,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,

                        ),
                    border = BorderStroke(
                        1.dp,
                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                    ),
                    trailingIcon = {
                        if (selected) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                )
            }
        }


//            AnimatedContent(
//                targetState = deepLink.folder,
//                label = "",
//                modifier = Modifier.fillMaxWidth(),
//            ) { folder ->
//                when (folder == null) {
//                    true -> DLLAssistChip(
//                        modifier = Modifier.fillMaxWidth(),
//                        onClick = { showSelectFolderBottomSheet = true },
//                        leadingIcon = {
//                            Icon(
//                                imageVector = Icons.Rounded.Add,
//                                contentDescription = "",
//                                modifier = Modifier.size(18.dp),
//                            )
//                        },
//                        trailingIcon = {
//                        },
//                        label = {
//                            Text(
//                                text = "Add Folder",
//                                modifier = Modifier.padding(vertical = 12.dp),
//                                style = MaterialTheme.typography.bodyMedium,
//                            )
//                        },
//                    )
//
//                    false -> DLLAssistChip(
//                        modifier = Modifier.fillMaxWidth(),
//                        onClick = onRemoveFolder,
//                        leadingIcon = {
//                            Icon(
//                                painterResource(Res.drawable.ic_folder_24dp),
//                                contentDescription = null,
//                                tint = MaterialTheme.colorScheme.secondary,
//                            )
//                        },
//                        trailingIcon = {
//                            DLLIconButton(onClick = onRemoveFolder) {
//                                Icon(
//                                    imageVector = Icons.Rounded.Clear,
//                                    contentDescription = "Remove folder",
//                                )
//                            }
//                        },
//                        label = {
//                            Text(
//                                text = folder.name,
//                                modifier = Modifier.padding(vertical = 12.dp).weight(1f),
//                                style = MaterialTheme.typography.bodyMedium.copy(
//                                    fontWeight = FontWeight.Bold,
//                                ),
//                            )
//                        },
//                    )
//                }
//            }


        Spacer(modifier = Modifier.padding(vertical = 24.dp))
    }
}

@Composable
private fun DeepLinkDetailsTextField(
    text: String,
    onTextChange: (String) -> Unit,
    label: String,
) {
    var textState by rememberSaveable { mutableStateOf(text) }

    DLLTextField(
        value = textState,
        onValueChange = {
            textState = it
            onTextChange(it)
        },
        label = label,
        modifier = Modifier.fillMaxWidth(),
    )
}
