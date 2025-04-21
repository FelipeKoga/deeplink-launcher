package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.addfolder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet
import dev.koga.deeplinklauncher.designsystem.DLLTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFolderBottomSheet(
    viewModel: AddFolderViewModel,
    onDismiss: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DLLModalBottomSheet(
        onDismiss = onDismiss,
    ) {
        AddFolderBottomSheetContent(
            uiState = uiState,
            onNameChanged = viewModel::onNameChanged,
            onDescriptionChanged = viewModel::onDescriptionChanged,
            onSubmit = viewModel::add
        )
    }
}

@Composable
fun AddFolderBottomSheetContent(
    uiState: AddFolderUiState,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 24.dp),
    ) {
        Text(
            text = "Add folder",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        Spacer(modifier = Modifier.height(24.dp))

        DLLTextField(
            value = uiState.name,
            onValueChange = onNameChanged,
            label = "Name",
        )

        Spacer(modifier = Modifier.height(12.dp))

        DLLTextField(
            modifier = Modifier.defaultMinSize(minHeight = 120.dp),
            value = uiState.description,
            onValueChange = onDescriptionChanged,
            label = "Description",
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { onSubmit() },
            ),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSubmit,
            enabled = uiState.isSubmitEnabled,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(.6f),
        ) {
            Text(text = "Save")
        }
    }
}
