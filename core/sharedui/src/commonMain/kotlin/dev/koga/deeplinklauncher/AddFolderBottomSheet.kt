package dev.koga.deeplinklauncher

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFolderBottomSheet(
    onDismiss: () -> Unit,
    onAdd: (name: String, description: String) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        AddFolderBottomSheetContent(onAdd = onAdd)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddFolderBottomSheetContent(
    onAdd: (name: String, description: String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val (name, setName) = rememberSaveable { mutableStateOf("") }
    val (description, setDescription) = rememberSaveable { mutableStateOf("") }


    Column(
        modifier = Modifier.padding(32.dp)
    ) {
        Text(
            text = "Add Folder", style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        dev.koga.deeplinklauncher.DLLTextField(
            value = name,
            onValueChange = setName,
            label = "Name",
        )

        Spacer(modifier = Modifier.height(12.dp))

        dev.koga.deeplinklauncher.DLLTextField(
            modifier = Modifier.defaultMinSize(minHeight = 120.dp),
            value = description,
            onValueChange = setDescription,
            label = "Description",
            imeAction = ImeAction.Done,
            onDone = { keyboardController?.hide() },
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onAdd(name, description) },
            enabled = name.isNotBlank(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(.6f)
        ) {
            Text(text = "Save")
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}