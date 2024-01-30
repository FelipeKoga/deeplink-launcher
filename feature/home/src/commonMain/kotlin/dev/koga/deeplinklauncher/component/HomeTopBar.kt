package dev.koga.deeplinklauncher.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.deeplinklauncher.DLLTopBar
import dev.koga.resources.MR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onExportScreen: () -> Unit,
    onImportScreen: () -> Unit,
) {
    var openImportExportOptionBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    DLLTopBar(
        scrollBehavior = scrollBehavior,
        title = "Deeplink Launcher",
        actions = {
            FilledTonalIconButton(
                onClick = {
                    openImportExportOptionBottomSheet = true
                },
            ) {
                Icon(
                    painter = painterResource(MR.images.ic_import_export_24dp),
                    contentDescription = "import or export deeplinks",
                    modifier = Modifier.size(18.dp),
                )
            }

            DropdownMenu(
                expanded = openImportExportOptionBottomSheet,
                onDismissRequest = { openImportExportOptionBottomSheet = false },
            ) {
                DropdownMenuItem(
                    text = { Text("Export") },
                    onClick = {
                        onExportScreen()
                        openImportExportOptionBottomSheet = false
                    },
                    leadingIcon = {
                        Icon(
                            painterResource(MR.images.ic_arrow_upward_24dp),
                            contentDescription = null,
                        )
                    },
                )

                DropdownMenuItem(
                    text = { Text("Import") },
                    onClick = {
                        onImportScreen()
                        openImportExportOptionBottomSheet = false
                    },
                    leadingIcon = {
                        Icon(
                            painterResource(MR.images.ic_arrow_downward_24dp),
                            contentDescription = null,
                        )
                    },
                )
            }
        },
    )
}
