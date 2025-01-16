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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import dev.koga.deeplinklauncher.LocalRootNavigator
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.DLLSingleChoiceSegmentedButtonRow
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults
import dev.koga.deeplinklauncher.importdata.ui.component.JSONBoxViewer
import dev.koga.deeplinklauncher.importexport.model.ExportFileType
import dev.koga.deeplinklauncher.permission.StoragePermission
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class ExportScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalRootNavigator.current
        val screenModel = getScreenModel<ExportScreenModel>()
        val storagePermission = koinInject<StoragePermission>()

        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        var selectedExportType by remember { mutableStateOf(ExportFileType.JSON) }
        var showPermissionRequest by remember { mutableStateOf(false) }
        val preview = screenModel.preview

        LaunchedEffect(Unit) {
            screenModel.messages.collectLatest { message ->
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short,
                )
            }
        }

        if (showPermissionRequest) {
            storagePermission.request()
            showPermissionRequest = false
        }

        Scaffold(
            topBar = {
                DLLTopBar(
                    title = {
                        DLLTopBarDefaults.title("Export DeepLinks")
                    },
                    navigationIcon = {
                        DLLTopBarDefaults.navigationIcon(onClicked = navigator::pop)
                    },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    isPermissionGranted = storagePermission.isGranted(),
                    export = {
                        scope.launch {
                            when (storagePermission.isGranted()) {
                                true -> {
                                    screenModel.export(selectedExportType)
                                }

                                false -> {
                                    showPermissionRequest = true
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun ExportContent(
    modifier: Modifier = Modifier,
    selectedExportType: ExportFileType,
    preview: ExportData,
    onChangeExportType: (ExportFileType) -> Unit,
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
            options = ExportFileType.entries.map { it.label }.toPersistentList(),
            selectedOption = selectedExportType.label,
            onOptionSelected = {
                onChangeExportType(ExportFileType.getByLabel(it))
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
                ExportFileType.JSON -> JSONBoxViewer(
                    text = preview.jsonFormat.ifEmpty { "No DeepLinks" },
                )

                ExportFileType.PLAIN_TEXT -> JSONBoxViewer(
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
