@file:OptIn(ExperimentalMaterial3Api::class)

package dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.DLLSingleChoiceSegmentedButtonRow
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults
import dev.koga.deeplinklauncher.file.model.FileType
import dev.koga.deeplinklauncher.file.model.getByLabel
import dev.koga.deeplinklauncher.file.model.label
import dev.koga.deeplinklauncher.datatransfer.impl.ui.component.JSONBoxViewer
import dev.koga.deeplinklauncher.navigation.popBackStack
import kotlinx.collections.immutable.toPersistentList

@Composable
fun ImportScreen(
    viewModel: ImportViewModel,
) {
    var showFilePicker by remember { mutableStateOf(false) }

    FilePicker(
        show = showFilePicker,
        fileExtensions = FileType.extensions,
    ) { platformFile ->
        showFilePicker = false
        viewModel.import(platformFile ?: return@FilePicker)
    }

    ImportUI(
        onBack = { viewModel.popBackStack() },
        onBrowse = { showFilePicker = true },
    )
}

@Composable
private fun ImportUI(
    onBrowse: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            DLLTopBar(
                title = {
                    DLLTopBarDefaults.title("Import DeepLinks")
                },
                navigationIcon = {
                    DLLTopBarDefaults.navigationIcon(onClicked = onBack)
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding).fillMaxSize()) {
            ImportContent(modifier = Modifier.weight(1f))

            ImportFooter(
                onBrowse = onBrowse,
            )
        }
    }
}

@Composable
private fun ImportContent(modifier: Modifier = Modifier) {
    var selectedType by remember { mutableStateOf(FileType.JSON) }

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
            options = FileType.entries.map { it.label }.toPersistentList(),
            selectedOption = selectedType.label,
            onOptionSelected = { selectedType = FileType.getByLabel(it) },
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
                FileType.JSON -> JSONTutorial()
                FileType.TXT -> PlainTextTutorial()
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

        JSONBoxViewer(
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

        JSONBoxViewer(text = jsonStructurePreview)

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

        JSONBoxViewer(
            text = plainTextPreview,
        )
    }
}
