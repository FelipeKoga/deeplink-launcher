package dev.koga.deeplinklauncher.deeplink.impl.ui.folderbatchtest.state

import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestProgress
import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestVerificationStatus
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ExportBatchTestReport

internal sealed interface FolderBatchTestAction {
    data object Start : FolderBatchTestAction
    data object Stop : FolderBatchTestAction
    data object Pass : FolderBatchTestAction
    data object Fail : FolderBatchTestAction
    data object Skip : FolderBatchTestAction
    data class ExportReport(val format: ExportBatchTestReport.Format) : FolderBatchTestAction
    data object DismissSummary : FolderBatchTestAction
}

internal data class FolderBatchTestUiState(
    val folderName: String = "",
    val isRunning: Boolean = false,
    val progress: BatchTestProgress? = null,
    val awaitingManualVerification: BatchTestProgress.AwaitingManualVerification? = null,
    val summary: BatchTestProgress.Finished? = null,
    val errorMessage: String? = null,
    val exportedFileName: String? = null,
)
