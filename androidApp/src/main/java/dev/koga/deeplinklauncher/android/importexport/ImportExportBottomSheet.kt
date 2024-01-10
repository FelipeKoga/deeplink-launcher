@file:OptIn(ExperimentalMaterial3Api::class)

package dev.koga.deeplinklauncher.android.importexport

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.koga.deeplinklauncher.android.theme.AppTheme

@Composable
fun ImportExportBottomSheet() {
    ModalBottomSheet(
        onDismissRequest = { /*TODO*/ },
        modifier = Modifier.fillMaxSize(.4f),
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        ImportExportContent()
    }
}

@Composable
private fun ImportExportContent() {
    var isImportChecked by rememberSaveable { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {

        TabRow(
            selectedTabIndex = if (isImportChecked) 0 else 1,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(selected = isImportChecked, onClick = { isImportChecked = true }) {
                Text(text = "Import")
            }

            Tab(selected = !isImportChecked, onClick = { isImportChecked = false }) {
                Text(text = "Export")
            }

        }

        when (isImportChecked) {
            true -> ImportContent()
            false -> ExportContent()
        }

    }
}

@Composable
fun ImportContent() {

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Tutorial")



    }

}

@Composable
fun ExportContent() {


}

@Preview(showBackground = true)
@Composable
fun ImportExportContentPreview() {
    AppTheme {
        ImportExportContent()
    }
}