package dev.koga.deeplinklauncher.folder

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.DLLModalBottomSheet
import dev.koga.deeplinklauncher.button.DLLOutlinedIconButton
import dev.koga.deeplinklauncher.model.Folder
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFolderBottomSheet(
    folders: ImmutableList<Folder>,
    onClick: (Folder) -> Unit,
    onDismissRequest: () -> Unit,
    onAdd: (name: String, description: String) -> Unit,
) {
    var showAddFolderUI by rememberSaveable {
        mutableStateOf(folders.isEmpty())
    }

    DLLModalBottomSheet(onDismiss = onDismissRequest) {
        AnimatedContent(targetState = showAddFolderUI, label = "") { target ->
            if (target) {
                AddFolderBottomSheetContent(
                    onAdd = { name, description ->
                        showAddFolderUI = false
                        onAdd(name, description)
                    },
                    showNavBack = true,
                    onBack = { showAddFolderUI = false },
                )
            } else {
                SelectFolderContent(
                    folders = folders,
                    onClick = onClick,
                    onAdd = { showAddFolderUI = true },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFolderContent(
    folders: ImmutableList<Folder>,
    onClick: (Folder) -> Unit,
    onAdd: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Select a folder",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.weight(1f),
                )

                DLLOutlinedIconButton(onClick = onAdd) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add folder",
                    )
                }
            }
        }

        items(folders) {
            OutlinedCard(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(.3.dp, MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                onClick = {
                    onClick(it)
                },
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "${it.deepLinkCount} deeplinks",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
