package dev.koga.deeplinklauncher.folder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.DLLModalBottomSheet
import dev.koga.deeplinklauncher.DLLTextField
import dev.koga.deeplinklauncher.button.DLLIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFolderBottomSheet(
    onDismiss: () -> Unit,
    onAdd: (name: String, description: String) -> Unit,
) {
    DLLModalBottomSheet(
        onDismiss = onDismiss,
    ) {
        AddFolderBottomSheetContent(
            onAdd = onAdd,
            showNavBack = false,
        )
    }
}

@Composable
fun AddFolderBottomSheetContent(
    onAdd: (name: String, description: String) -> Unit,
    showNavBack: Boolean,
    onBack: () -> Unit = {},
) {
    val (name, setName) = rememberSaveable { mutableStateOf("") }
    val (description, setDescription) = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 24.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showNavBack) {
                DLLIconButton(onClick = onBack) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                }

                Spacer(modifier = Modifier.width(12.dp))
            }

            Text(
                text = "Add folder",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        DLLTextField(
            value = name,
            onValueChange = setName,
            label = "Name",
        )

        Spacer(modifier = Modifier.height(12.dp))

        DLLTextField(
            modifier = Modifier.defaultMinSize(minHeight = 120.dp),
            value = description,
            onValueChange = setDescription,
            label = "Description",
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { onAdd(name, description) },
            ),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onAdd(name, description) },
            enabled = name.isNotBlank(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(.6f),
        ) {
            Text(text = "Save")
        }
    }
}
