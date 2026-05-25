@file:OptIn(ExperimentalMaterial3Api::class)

package dev.koga.deeplinklauncher.deeplink.impl.ui.folderbatchtest

import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestLaunchStatus
import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestProgress
import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestVerificationStatus
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ExportBatchTestReport
import dev.koga.deeplinklauncher.deeplink.api.ui.formatting.truncatedLink
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderbatchtest.state.FolderBatchTestAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderbatchtest.state.FolderBatchTestUiState
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults
import dev.koga.deeplinklauncher.designsystem.button.DLLButton
import dev.koga.deeplinklauncher.designsystem.button.DLLButtonVariant
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.AppRoute

@Composable
internal fun FolderBatchTestScreen(
    viewModel: FolderBatchTestViewModel,
    appNavigator: AppNavigator,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FolderBatchTestUI(
        uiState = uiState,
        onAction = viewModel::onAction,
        onNavigateBack = { appNavigator.navigate(AppRoute.PopBackStack) },
    )
}

@Composable
internal fun FolderBatchTestUI(
    uiState: FolderBatchTestUiState,
    onAction: (FolderBatchTestAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val progress = uiState.progress
    val currentIndex = when (progress) {
        is BatchTestProgress.LinkStarted -> progress.index + 1
        is BatchTestProgress.LaunchCompleted -> progress.index + 1
        is BatchTestProgress.AwaitingManualVerification -> progress.index + 1
        is BatchTestProgress.LinkCompleted -> progress.index + 1
        is BatchTestProgress.Started -> 0
        else -> 0
    }
    val totalLinks = when (progress) {
        is BatchTestProgress.LinkStarted -> progress.total
        is BatchTestProgress.LaunchCompleted -> progress.total
        is BatchTestProgress.AwaitingManualVerification -> progress.total
        is BatchTestProgress.LinkCompleted -> progress.total
        is BatchTestProgress.Started -> progress.totalLinks
        is BatchTestProgress.Finished -> progress.results.size
        else -> 0
    }

    Scaffold(
        containerColor = colors.surface.background,
        topBar = {
            DLLTopBar(
                title = { Text(text = "Batch test") },
                navigationIcon = {
                    DLLTopBarDefaults.NavigationIcon(onClicked = onNavigateBack)
                },
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = uiState.folderName,
                style = typography.title.page,
            )

            if (totalLinks > 0 && uiState.isRunning) {
                Text(
                    text = "Progress: $currentIndex / $totalLinks",
                    style = typography.label.caption,
                )
                LinearProgressIndicator(
                    progress = { currentIndex.toFloat() / totalLinks.toFloat() },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    style = typography.body.default.copy(color = colors.text.error),
                )
            }

            when (val awaiting = uiState.awaitingManualVerification) {
                null -> Unit
                else -> ManualVerificationSection(awaiting, onAction)
            }

            progress?.let { batchProgress ->
                BatchProgressDetails(batchProgress)
            }

            uiState.summary?.let { summary ->
                SummarySection(summary, uiState.exportedFileName, onAction)
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (!uiState.isRunning && uiState.summary == null) {
                DLLButton(
                    onClick = { onAction(FolderBatchTestAction.Start) },
                    text = "Start batch test",
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            if (uiState.isRunning) {
                DLLButton(
                    onClick = { onAction(FolderBatchTestAction.Stop) },
                    text = "Stop",
                    variant = DLLButtonVariant.Secondary,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ManualVerificationSection(
    awaiting: BatchTestProgress.AwaitingManualVerification,
    onAction: (FolderBatchTestAction) -> Unit,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val deepLink = awaiting.deepLink

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Verify on device",
            style = typography.title.card,
        )
        Text(
            text = deepLink.name?.takeIf { it.isNotBlank() } ?: deepLink.truncatedLink(),
            style = typography.body.default,
        )
        Text(
            text = deepLink.link,
            style = typography.code.link.copy(color = colors.text.muted),
        )

        awaiting.autoDetails?.let {
            Text(
                text = "Auto check: $it",
                style = typography.body.default.copy(color = colors.text.error),
            )
        }

        awaiting.foregroundPackage?.let {
            Text(text = "Package: $it", style = typography.label.caption)
        }
        awaiting.foregroundActivity?.let {
            Text(text = "Activity: $it", style = typography.label.caption)
        }

        if (awaiting.capturedTexts.isNotEmpty()) {
            Text(
                text = "Visible text: ${awaiting.capturedTexts.joinToString(", ")}",
                style = typography.label.caption,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DLLButton(onClick = { onAction(FolderBatchTestAction.Pass) }, text = "Pass")
            DLLButton(
                onClick = { onAction(FolderBatchTestAction.Fail) },
                text = "Fail",
                variant = DLLButtonVariant.Secondary,
            )
            DLLButton(
                onClick = { onAction(FolderBatchTestAction.Skip) },
                text = "Skip",
                variant = DLLButtonVariant.Secondary,
            )
        }
    }
}

@Composable
private fun BatchProgressDetails(progress: BatchTestProgress) {
    val typography = DeepLinkTheme.typography
    val statusText = when (progress) {
        is BatchTestProgress.Started -> "Running on ${progress.deviceName}"
        is BatchTestProgress.LinkStarted -> "Launching ${progress.deepLink.truncatedLink()}"
        is BatchTestProgress.LaunchCompleted -> when (val status = progress.launchStatus) {
            BatchTestLaunchStatus.Success -> "Launch OK"
            is BatchTestLaunchStatus.Failure -> "Launch failed: ${status.message}"
        }
        is BatchTestProgress.AwaitingManualVerification -> "Waiting for manual verification"
        is BatchTestProgress.LinkCompleted -> verificationLabel(progress.result.verificationStatus)
        is BatchTestProgress.Finished -> "Batch finished"
        is BatchTestProgress.Error -> "Error"
        is BatchTestProgress.Stopped -> "Batch stopped"
    }

    Text(text = statusText, style = typography.body.default)
}

@Composable
private fun SummarySection(
    summary: BatchTestProgress.Finished,
    exportedFileName: String?,
    onAction: (FolderBatchTestAction) -> Unit,
) {
    val typography = DeepLinkTheme.typography

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Summary", style = typography.title.card)
        Text(text = "Passed: ${summary.passed}", style = typography.body.default)
        Text(text = "Failed: ${summary.failed}", style = typography.body.default)
        Text(text = "Skipped: ${summary.skipped}", style = typography.body.default)

        exportedFileName?.let {
            Text(text = "Exported: $it", style = typography.label.caption)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DLLButton(
                onClick = { onAction(FolderBatchTestAction.ExportReport(ExportBatchTestReport.Format.JSON)) },
                text = "Export JSON",
                variant = DLLButtonVariant.Secondary,
            )
            DLLButton(
                onClick = { onAction(FolderBatchTestAction.ExportReport(ExportBatchTestReport.Format.CSV)) },
                text = "Export CSV",
                variant = DLLButtonVariant.Secondary,
            )
        }

        DLLButton(
            onClick = { onAction(FolderBatchTestAction.DismissSummary) },
            text = "Done",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private fun verificationLabel(status: BatchTestVerificationStatus): String = when (status) {
    BatchTestVerificationStatus.AutoPassed -> "Auto passed"
    is BatchTestVerificationStatus.AutoFailed -> "Auto failed"
    BatchTestVerificationStatus.ManualPassed -> "Manual passed"
    BatchTestVerificationStatus.ManualFailed -> "Manual failed"
    BatchTestVerificationStatus.Skipped -> "Skipped"
    BatchTestVerificationStatus.NotVerified -> "Not verified"
}
