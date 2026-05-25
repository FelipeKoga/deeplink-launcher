package dev.koga.deeplinklauncher.deeplink.impl.ui.folderbatchtest

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestProgress
import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestVerificationStatus
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ExportBatchTestReport
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.RunFolderBatchTest
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderbatchtest.state.FolderBatchTestAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderbatchtest.state.FolderBatchTestUiState
import dev.koga.deeplinklauncher.navigation.AppNavigator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class FolderBatchTestViewModel(
    savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository,
    private val runFolderBatchTest: RunFolderBatchTest,
    private val exportBatchTestReport: ExportBatchTestReport,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val folderId = savedStateHandle.toRoute<DeepLinkRouteEntryPoint.FolderBatchTest>().folderId
    private val folderName = folderRepository.getFolderById(folderId)?.name.orEmpty()

    private val ui = MutableStateFlow(
        FolderBatchTestUiState(
            folderName = folderName,
        ),
    )
    private var batchJob: Job? = null
    private var activeRunId: String? = null

    val uiState = ui.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ui.value,
    )

    fun onAction(action: FolderBatchTestAction) {
        when (action) {
            FolderBatchTestAction.Start -> startBatchTest()
            FolderBatchTestAction.Stop -> stopBatchTest()
            FolderBatchTestAction.Pass -> submitManual(BatchTestVerificationStatus.ManualPassed)
            FolderBatchTestAction.Fail -> submitManual(BatchTestVerificationStatus.ManualFailed)
            FolderBatchTestAction.Skip -> submitManual(BatchTestVerificationStatus.Skipped)
            is FolderBatchTestAction.ExportReport -> exportReport(action.format)
            FolderBatchTestAction.DismissSummary -> {
                ui.update { it.copy(summary = null, exportedFileName = null) }
                appNavigator.popBackStack()
            }
        }
    }

    private fun startBatchTest() {
        if (batchJob?.isActive == true) return

        batchJob = viewModelScope.launch {
            ui.update {
                it.copy(
                    isRunning = true,
                    errorMessage = null,
                    summary = null,
                    exportedFileName = null,
                    awaitingManualVerification = null,
                )
            }

            runFolderBatchTest(folderId).collect { progress ->
                activeRunId = progress.runId
                ui.update { state ->
                    state.copy(
                        progress = progress,
                        awaitingManualVerification = progress as? BatchTestProgress.AwaitingManualVerification,
                        summary = progress as? BatchTestProgress.Finished,
                        errorMessage = (progress as? BatchTestProgress.Error)?.message,
                        isRunning = progress !is BatchTestProgress.Finished &&
                            progress !is BatchTestProgress.Error &&
                            progress !is BatchTestProgress.Stopped,
                    )
                }
            }

            ui.update { it.copy(isRunning = false, awaitingManualVerification = null) }
        }
    }

    private fun stopBatchTest() {
        viewModelScope.launch {
            activeRunId?.let { runId ->
                runFolderBatchTest.stop(runId)
            }
        }
    }

    private fun submitManual(status: BatchTestVerificationStatus) {
        viewModelScope.launch {
            activeRunId?.let { runId ->
                runFolderBatchTest.submitManualVerification(runId, status)
            }
            ui.update { it.copy(awaitingManualVerification = null) }
        }
    }

    private fun exportReport(format: ExportBatchTestReport.Format) {
        val summary = ui.value.summary ?: return
        val report = ExportBatchTestReport.Report(
            runId = summary.runId,
            folderId = summary.folderId,
            deviceId = summary.deviceId,
            deviceName = summary.deviceName,
            results = summary.results,
        )
        when (val result = exportBatchTestReport(report, format)) {
            is ExportBatchTestReport.Result.Success -> {
                ui.update { it.copy(exportedFileName = result.fileName) }
            }

            is ExportBatchTestReport.Result.Error -> {
                ui.update {
                    it.copy(errorMessage = result.throwable.message ?: "Failed to export report")
                }
            }
        }
    }
}
