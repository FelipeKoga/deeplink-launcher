package dev.koga.deeplinklauncher.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import dev.koga.deeplinklauncher.BoxPreview
import dev.koga.deeplinklauncher.DLLHorizontalDivider
import dev.koga.deeplinklauncher.DLLSingleChoiceSegmentedButtonRow
import dev.koga.deeplinklauncher.DLLTopBar
import dev.koga.deeplinklauncher.file.GetFileRealPath
import dev.koga.deeplinklauncher.model.FileType
import dev.koga.deeplinklauncher.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.usecase.ImportDeepLinksOutput
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private enum class ImportType(val label: String) {
    JSON("JSON (.json)"),
    PLAIN_TEXT("Plain text (.txt)"),
    ;

    companion object {
        fun getByLabel(label: String): ImportType {
            return entries.first { it.label == label }
        }
    }
}

class ImportScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val importDeepLinks = koinInject<ImportDeepLinks>()
        val getFileRealPath = koinInject<GetFileRealPath>()

        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        var showFilePicker by remember { mutableStateOf(false) }

        FilePicker(
            show = showFilePicker,
            fileExtensions = FileType.extensions,
        ) { platformFile ->
            showFilePicker = false

            scope.launch {
                val path = getFileRealPath.get(platformFile?.path!!)

                val fileType = when (path.substringAfterLast(".")) {
                    FileType.TXT.extension -> FileType.TXT
                    FileType.JSON.extension -> FileType.JSON
                    else -> {
                        snackbarHostState.showSnackbar("Unsupported file type")

                        return@launch
                    }
                }

                val response = importDeepLinks.invoke(
                    filePath = path,
                    fileType = fileType,
                )

                when (response) {
                    is ImportDeepLinksOutput.Success -> {
                        snackbarHostState.showSnackbar("DeepLinks imported successfully")
                    }

                    is ImportDeepLinksOutput.Error -> {
                        snackbarHostState.showSnackbar(
                            "Something went wrong. " +
                                    "Check the content structure and try again.",
                        )
                    }
                }
            }
        }

        Scaffold(
            topBar = {
                DLLTopBar(
                    title = "Import DeepLinks",
                    onNavigationActionClicked = navigator::pop,
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
            containerColor = MaterialTheme.colorScheme.background,
        ) { contentPadding ->
            Column(modifier = Modifier.padding(contentPadding).fillMaxSize()) {
                ImportContent(
                    modifier = Modifier.weight(1f),
                )

                ImportFooter {
                    showFilePicker = true
                }
            }
        }
    }
}

@Composable
fun ImportContent(modifier: Modifier = Modifier) {
    var selectedType by remember { mutableStateOf(ImportType.JSON) }

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            "How to Import Data",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        val headerText = buildAnnotatedString {
            append("The are two types of files that can be imported: ")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append("plain text (.txt)")
            }

            append(" and ")

            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append("JSON (.json)")
            }
        }

        Text(
            text = headerText,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Normal,
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        DLLSingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            options = ImportType.entries.map { it.label }.toPersistentList(),
            selectedOption = selectedType.label,
            onOptionSelected = { selectedType = ImportType.getByLabel(it) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedContent(
            targetState = selectedType,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    slideInHorizontally { width -> -width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> width } + fadeOut()
                }.using(
                    SizeTransform(clip = false),
                )
            },
            label = "",
        ) { selectedType ->
            when (selectedType) {
                ImportType.JSON -> JSONTutorial()
                ImportType.PLAIN_TEXT -> PlainTextTutorial()
            }
        }
    }
}

@Composable
fun ImportFooter(modifier: Modifier = Modifier, onBrowse: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DLLHorizontalDivider()

        Button(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            onClick = onBrowse,
        ) {
            Text(text = "Browse file")
        }
    }
}

@Composable
fun JSONTutorial() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "The most basic JSON format is an object that only " +
                    "contains a link property.",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Normal,
            ),
        )

        BoxPreview(
            text = basicJsonPreview,
        )

        Text(
            text = generalPropertiesHint,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Normal,
            ),
        )

        Text(
            text = folderPropertiesHint,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Normal,
            ),
        )

        Text(
            text = uuidHint,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Normal,
            ),
        )

        Text(
            text = createdAtHint,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Normal,
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "JSON structure:",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Normal,
            ),
        )

        BoxPreview(text = jsonStructurePreview)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun PlainTextTutorial() {
    Column {
        Text(
            text = "The plain text format is a simple list of deeplinks, " +
                    "one per line.",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Normal,
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        BoxPreview(
            text = plainTextPreview,
        )
    }
}
