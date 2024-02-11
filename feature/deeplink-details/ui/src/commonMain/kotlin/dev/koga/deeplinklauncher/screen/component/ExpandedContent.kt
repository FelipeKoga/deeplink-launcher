package dev.koga.deeplinklauncher.screen.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.deeplinklauncher.DLLTextField
import dev.koga.deeplinklauncher.folder.SelectFolderBottomSheet
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.screen.state.DeepLinkDetailsUiState
import dev.koga.resources.MR

@Composable
fun DeepLinkDetailsExpandedContent(
    modifier: Modifier,
    uiState: DeepLinkDetailsUiState,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onAddFolder: (String, String) -> Unit,
    onSelectFolder: (Folder) -> Unit,
    onRemoveFolder: () -> Unit,
    onDeleteDeepLink: () -> Unit,
    onCollapse: () -> Unit,
) {
    val deepLink = uiState.deepLink

    var showSelectFolderBottomSheet by remember {
        mutableStateOf(false)
    }

    if (showSelectFolderBottomSheet) {
        SelectFolderBottomSheet(
            folders = uiState.folders,
            onDismissRequest = {
                showSelectFolderBottomSheet = false
            },
            onAdd = { name, description ->
                onAddFolder(name, description)
                showSelectFolderBottomSheet = false
            },
            onClick = {
                onSelectFolder(it)
                showSelectFolderBottomSheet = false
            },
        )
    }

    SelectionContainer(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onDeleteDeepLink,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete deeplink",
                    )
                }

                FilledTonalIconButton(
                    modifier = Modifier,
                    onClick = onCollapse
                ) {
                    Icon(
                        painterResource(MR.images.ic_unfold_less_24dp),
                        contentDescription = "Unfold less",
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Name",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            DeepLinkDetailsTextField(
                text = deepLink.name.orEmpty(),
                onTextChange = onDescriptionChanged,
            )

//            EditableText(
//                value = deepLink.name.orEmpty(),
//                onSave = onNameChanged,
//                modifier = Modifier.fillMaxWidth(),
//                inputLabel = "Enter a name",
//            ) {
//                Text(
//                    text = deepLink.name.orEmpty().ifEmpty { "--" },
//                    style = MaterialTheme.typography.headlineSmall.copy(
//                        fontWeight = FontWeight.SemiBold,
//                        color = MaterialTheme.colorScheme.onBackground,
//                    ),
//                )
//            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Description",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            DeepLinkDetailsTextField(
                text = deepLink.description.orEmpty(),
                onTextChange = onDescriptionChanged,
            )
//
//            EditableText(
//                value = deepLink.description.orEmpty(),
//                onSave = onDescriptionChanged,
//                modifier = Modifier.fillMaxWidth(),
//                inputLabel = "Enter a description",
//            ) {
//                Text(
//                    text = deepLink.description.orEmpty().ifEmpty { "--" },
//                    style = MaterialTheme.typography.bodyMedium.copy(
//                        fontWeight = FontWeight.Normal,
//                        color = MaterialTheme.colorScheme.onBackground,
//                    ),
//                )
//            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedContent(
                targetState = deepLink.folder,
                label = "",
                modifier = Modifier.fillMaxWidth(),
            ) { folder ->

                when (folder) {
                    null -> ElevatedAssistChip(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = { showSelectFolderBottomSheet = true },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "",
                                modifier = Modifier.size(18.dp),
                            )
                        },
                        label = { Text(text = "Add Folder") },
                    )

                    else -> ElevatedAssistChip(
                        colors = AssistChipDefaults.elevatedAssistChipColors(),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showSelectFolderBottomSheet = true },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(MR.images.ic_folder_24dp),
                                contentDescription = null,
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "Remove folder",
                                modifier = Modifier.clickable {
                                    onRemoveFolder()
                                },
                            )
                        },
                        label = {
                            Text(
                                text = folder.name,
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold,
                            )
                        },
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 24.dp))

            Text(
                text = "DeepLink",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            )

            Text(
                text = deepLink.link,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            )

            Spacer(modifier = Modifier.padding(vertical = 24.dp))
        }
    }
}

@Composable
fun DeepLinkDetailsTextField(
    text: String,
    onTextChange: (String) -> Unit,
) {
    var textState by rememberSaveable { mutableStateOf(text) }

    DLLTextField(
        value = textState,
        onValueChange = {
            textState = it
            onTextChange(it)
        },
        label = "Enter a name",
        modifier = Modifier.fillMaxWidth(),
    )
}