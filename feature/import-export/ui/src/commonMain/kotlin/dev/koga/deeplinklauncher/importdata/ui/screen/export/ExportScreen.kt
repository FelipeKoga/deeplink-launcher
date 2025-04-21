package dev.koga.deeplinklauncher.importdata.ui.screen.export

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.DLLSingleChoiceSegmentedButtonRow
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults
import dev.koga.deeplinklauncher.file.model.FileType
import dev.koga.deeplinklauncher.importdata.ui.component.JSONBoxViewer
import dev.koga.deeplinklauncher.importdata.ui.utils.getByLabel
import dev.koga.deeplinklauncher.importdata.ui.utils.label
import dev.koga.deeplinklauncher.navigation.AppNavigationRoute
import kotlinx.collections.immutable.toPersistentList

@Composable
fun ExportScreen(
    viewModel: ExportViewModel,
) {
    var showPermissionRequest by remember { mutableStateOf(false) }

    LaunchedEffect(showPermissionRequest) {
    }

    ExportUI(
        preview = viewModel.preview,
        onExport = viewModel::export,
        onBack = { viewModel.navigate(AppNavigationRoute.Back) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportUI(
    preview: ExportData,
    onExport: (FileType) -> Unit,
    onBack: () -> Unit,
) {
    var selectedExportType by remember { mutableStateOf(FileType.JSON) }
//    LaunchedEffect(Unit) {
//        screenModel.messages.collectLatest { message ->
//            snackBarHostState.showSnackbar(
//                message = message,
//                duration = SnackbarDuration.Short,
//            )
//        }
//    }

    Scaffold(
        topBar = {
            DLLTopBar(
                title = {
                    DLLTopBarDefaults.title("Export DeepLinks")
                },
                navigationIcon = {
                    DLLTopBarDefaults.navigationIcon(onClicked = onBack)
                },
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            ExportContent(
                modifier = Modifier.weight(1f),
                preview = preview,
                selectedExportType = selectedExportType,
                onChangeExportType = {
                    selectedExportType = it
                },
            )

            ExportFooter(
                isPermissionGranted = true,
                export = { onExport(selectedExportType) },
            )
        }
    }
}

@Composable
fun ExportContent(
    modifier: Modifier = Modifier,
    selectedExportType: FileType,
    preview: ExportData,
    onChangeExportType: (FileType) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 8.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Select the file type to export",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        DLLSingleChoiceSegmentedButtonRow(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            options = FileType.entries.map { it.label }.toPersistentList(),
            selectedOption = selectedExportType.label,
            onOptionSelected = {
                onChangeExportType(FileType.getByLabel(it))
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Preview",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedContent(
            targetState = selectedExportType,
            label = "",
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
        ) { index ->
            when (index) {
                FileType.JSON -> JSONBoxViewer(
                    text = preview.jsonFormat.ifEmpty { "No DeepLinks" },
                )

                FileType.TXT -> JSONBoxViewer(
                    text = preview.plainTextFormat.ifEmpty { "No DeepLinks" },
                )
            }
        }
    }
}

@Composable
fun ExportFooter(
    modifier: Modifier = Modifier,
    isPermissionGranted: Boolean,
    export: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DLLHorizontalDivider()

        Button(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            onClick = export,
        ) {
            Text(
                text = when (isPermissionGranted) {
                    true -> "Export"
                    false -> "Grant permission"
                },
            )
        }
    }
}
