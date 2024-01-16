package dev.koga.deeplinklauncher.android.import

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.koga.deeplinklauncher.android.core.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.usecase.FileType
import dev.koga.deeplinklauncher.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.usecase.ImportDeepLinksOutput
import dev.koga.deeplinklauncher.util.getRealPathFromUri
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class ImportScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val importDeepLinks = koinInject<ImportDeepLinks>()

        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val context = LocalContext.current

        val filePickerLauncher = rememberLauncherForActivityResult(
            contract = object : ActivityResultContracts.GetContent() {
                override fun createIntent(context: Context, input: String): Intent {
                    return super.createIntent(context, input)
                        .putExtra(
                            Intent.EXTRA_MIME_TYPES,
                            arrayOf("text/plain", "application/json")
                        )
                }
            },
            onResult = { uri ->
                // Handle the URI, convert it to a file path or process it directly
                val filePath = uri?.getRealPathFromUri(context).orEmpty()
                scope.launch {
                    val response = importDeepLinks.invoke(
                        filePath = filePath,
                        fileType = when (filePath.substringAfterLast(".")) {
                            "txt" -> FileType.TXT
                            "json" -> FileType.JSON
                            else -> throw IllegalStateException("Invalid file type")
                        }
                    )

                    when (response) {
                        is ImportDeepLinksOutput.Success -> {
                            snackbarHostState.showSnackbar("DeepLinks imported successfully")
                            navigator.pop()
                        }

                        is ImportDeepLinksOutput.Error -> {
                            snackbarHostState.showSnackbar("Something went wrong")
                        }
                    }
                }
            }
        )

        Scaffold(
            topBar =
            {
                DLLTopBar(title = "Import DeepLinks", onBack = navigator::pop)
            },
            snackbarHost =
            { SnackbarHost(snackbarHostState) }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(24.dp)
            ) {

                Text(
                    text = "There are two types of files that can be imported: .txt and .json.",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "The file must have the following format:")
                Text(text = "DeepLink1")
                Text(text = "DeepLink2")

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        filePickerLauncher.launch("*/*") // Launch the picker
                    }) {
                    Text(text = "Browse")
                }

                Spacer(modifier = Modifier.weight(1f))

            }
        }
    }
}