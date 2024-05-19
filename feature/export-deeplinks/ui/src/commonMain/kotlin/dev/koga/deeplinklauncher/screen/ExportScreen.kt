package dev.koga.deeplinklauncher.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.koga.deeplinklauncher.BoxPreview
import dev.koga.deeplinklauncher.DLLSingleChoiceSegmentedButtonRow
import dev.koga.deeplinklauncher.DLLTopBar
import dev.koga.deeplinklauncher.model.ExportFileType
import dev.koga.deeplinklauncher.util.shouldAskForPermission
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExportScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val permissionFactory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
        val permissionController: PermissionsController = remember(permissionFactory) {
            permissionFactory.createPermissionsController()
        }

        val scope = rememberCoroutineScope()

        val snackbarHostState = remember { SnackbarHostState() }

        val screenModel = getScreenModel<ExportScreenModel>()
        val preview = screenModel.preview

        var selectedExportType by remember { mutableStateOf(ExportFileType.JSON) }
        var isPermissionGranted by remember { mutableStateOf(false) }

        val scrollBehavior =
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

        LaunchedEffect(Unit) {
            screenModel.messages.collectLatest { message ->
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short,
                )
            }
        }

        BindEffect(permissionController)

        LaunchedEffect(true) {
            isPermissionGranted = !shouldAskForPermission(
                permissionsController = permissionController,
                permission = Permission.WRITE_STORAGE,
            )
        }

        Scaffold(
            topBar = {
                DLLTopBar(
                    scrollBehavior = scrollBehavior,
                    title = "Export DeepLinks",
                    onNavigationActionClicked = navigator::pop,
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.surface,
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
                    nestedScrollConnection = scrollBehavior.nestedScrollConnection,
                    onChangeExportType = {
                        selectedExportType = it
                    }
                )

                ExportFooter(
                    isPermissionGranted = isPermissionGranted,
                    export = {
                        scope.launch {
                            when (isPermissionGranted) {
                                true -> {
                                    screenModel.export(selectedExportType)
                                }

                                false -> {
                                    scope.launch {
                                        try {
                                            permissionController.providePermission(Permission.WRITE_STORAGE)

                                            isPermissionGranted = true
                                        } catch (e: DeniedAlwaysException) {
                                            val result = snackbarHostState.showSnackbar(
                                                message = "Permission denied always. " +
                                                        "Please enable it in settings",
                                                duration = SnackbarDuration.Short,
                                                actionLabel = "Settings",
                                            )

                                            when (result) {
                                                SnackbarResult.Dismissed -> Unit
                                                SnackbarResult.ActionPerformed ->
                                                    permissionController.openAppSettings()
                                            }
                                        } catch (e: DeniedException) {
                                            snackbarHostState.showSnackbar(
                                                message = "Permission denied",
                                                duration = SnackbarDuration.Short,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ExportContent(
    modifier: Modifier = Modifier,
    nestedScrollConnection: NestedScrollConnection,
    selectedExportType: ExportFileType,
    preview: ExportData,
    onChangeExportType: (ExportFileType) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
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
                ExportFileType.JSON -> BoxPreview(
                    text = preview.jsonFormat.ifEmpty { "No DeepLinks" },
                )

                ExportFileType.PLAIN_TEXT -> BoxPreview(
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
    export: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Divider()

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
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
