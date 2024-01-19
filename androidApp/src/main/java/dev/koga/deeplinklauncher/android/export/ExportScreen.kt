package dev.koga.deeplinklauncher.android.export

import android.app.DownloadManager
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import dev.koga.deeplinklauncher.android.import.JSONBoxViewer
import dev.koga.deeplinklauncher.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.usecase.ExportDeepLinksOutput
import dev.koga.deeplinklauncher.usecase.FileType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class ExportScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        val screenModel = getScreenModel<ExportScreenModel>()
        val preview = screenModel.preview

        val exportDeepLinks = koinInject<ExportDeepLinks>()

        var selectedIndex by remember { mutableIntStateOf(0) }
        val options = listOf("JSON (.json)", "Plain text (.txt)")

        Scaffold(
            topBar = {
                DLLTopBar(title = "Export DeepLinks", onBack = navigator::pop)
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.surface,
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    Text(
                        text = "Select the file type to export",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                    ) {
                        options.forEachIndexed { index, label ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = options.size
                                ),
                                onClick = { selectedIndex = index },
                                selected = index == selectedIndex
                            ) {
                                Text(label)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Preview",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AnimatedContent(targetState = selectedIndex, label = "",
                        transitionSpec = {
                            if (targetState > initialState) {
                                slideInHorizontally { width -> width } + fadeIn() togetherWith
                                        slideOutHorizontally { width -> -width } + fadeOut()
                            } else {
                                slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                        slideOutHorizontally { width -> width } + fadeOut()
                            }.using(
                                SizeTransform(clip = false)
                            )
                        }
                    ) { index ->
                        when (index) {
                            0 -> JSONBoxViewer(text = preview.jsonFormat)
                            1 -> JSONBoxViewer(text = preview.plainTextFormat)
                        }
                    }

                    Spacer(modifier = Modifier.height(82.dp))
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    HorizontalDivider()

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        onClick = {
                            scope.launch {
                                val response = exportDeepLinks.export(
                                    type = when (selectedIndex) {
                                        0 -> FileType.TXT
                                        1 -> FileType.JSON
                                        else -> throw IllegalStateException("Invalid index")
                                    }
                                )

                                when (response) {
                                    ExportDeepLinksOutput.Empty -> snackbarHostState.showSnackbar(
                                        message = "No DeepLinks to export.",
                                    )

                                    is ExportDeepLinksOutput.Error -> snackbarHostState.showSnackbar(
                                        message = "Something went wrong. "
                                    )

                                    ExportDeepLinksOutput.Success -> {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "DeepLinks exported successfully. " +
                                                    "Check your downloads folder.",
                                            actionLabel = "Open",
                                            duration = SnackbarDuration.Short
                                        )

                                        when (result) {
                                            SnackbarResult.Dismissed -> Unit
                                            SnackbarResult.ActionPerformed -> {
                                                context.startActivity(
                                                    Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Export")
                    }
                }
            }
        }
    }
}