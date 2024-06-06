package dev.koga.deeplinklauncher.screen.details.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
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
import dev.koga.deeplinklauncher.DLLAssistChip
import dev.koga.deeplinklauncher.DLLHorizontalDivider
import dev.koga.deeplinklauncher.DLLTextField
import dev.koga.deeplinklauncher.button.DLLIconButton
import dev.koga.deeplinklauncher.folder.SelectFolderBottomSheet
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.screen.details.state.DeepLinkDetailsUiState
import dev.koga.resources.Res
import dev.koga.resources.ic_folder_24dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun EditModeUI(
    modifier: Modifier,
    uiState: DeepLinkDetailsUiState,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onLinkChanged: (String) -> Unit,
    onAddFolder: (String, String) -> Unit,
    onSelectFolder: (Folder) -> Unit,
    onRemoveFolder: () -> Unit,
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
                .verticalScroll(rememberScrollState()),
        ) {
            DeepLinkDetailsTextField(
                text = deepLink.name.orEmpty(),
                onTextChange = onNameChanged,
                label = "Name",
            )

            Spacer(modifier = Modifier.height(24.dp))

            DeepLinkDetailsTextField(
                text = deepLink.description.orEmpty(),
                onTextChange = onDescriptionChanged,
                label = "Description",
            )

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedContent(
                targetState = deepLink.folder,
                label = "",
                modifier = Modifier.fillMaxWidth(),
            ) { folder ->
                when (folder == null) {
                    true -> DLLAssistChip(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showSelectFolderBottomSheet = true },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "",
                                modifier = Modifier.size(18.dp),
                            )
                        },
                        trailingIcon = {
                        },
                        label = {
                            Text(
                                text = "Add Folder",
                                modifier = Modifier.padding(vertical = 12.dp),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                    )

                    false -> DLLAssistChip(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onRemoveFolder,
                        leadingIcon = {
                            Icon(
                                painterResource(Res.drawable.ic_folder_24dp),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                            )
                        },
                        trailingIcon = {
                            DLLIconButton(onClick = onRemoveFolder) {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = "Remove folder",
                                )
                            }
                        },
                        label = {
                            Text(
                                text = folder.name,
                                modifier = Modifier.padding(vertical = 12.dp).weight(1f),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                ),
                            )
                        },
                    )
                }
            }

            DLLHorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))


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

            Spacer(modifier = Modifier.padding(vertical = 24.dp))
        }
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
