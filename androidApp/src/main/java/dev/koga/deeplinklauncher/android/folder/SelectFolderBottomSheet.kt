package dev.koga.deeplinklauncher.android.folder

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.android.core.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.model.Folder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFolderBottomSheet(
    folders: List<Folder>,
    onClick: (Folder) -> Unit,
    onDismissRequest: () -> Unit,
    onAdd: (name: String, description: String) -> Unit,
) {

    var showAddFolderUI by rememberSaveable {
        mutableStateOf(folders.isEmpty())
    }

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        AnimatedContent(targetState = showAddFolderUI, label = "") { target ->
            if (target) {
                AddFolderBottomSheetContent(
                    onAdd = { name, description ->
                        showAddFolderUI = false
                        onAdd(name, description)
                    }
                )
            } else {
                Scaffold(topBar = {
                    DLLTopBar(title = "Select a folder", actions = {
                        FilledTonalIconButton(onClick = { showAddFolderUI = true }) {
                            Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                        }
                    })
                }) { contentPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(folders) {
                            ElevatedCard(
                                onClick = { onClick(it) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Column(modifier = Modifier.padding(24.dp)) {
                                    Text(
                                        text = it.name,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Spacer(modifier = Modifier.padding(4.dp))
                                    Text(
                                        text = "${it.deepLinkCount} deep links",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }

                }
            }

        }


    }
}