package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestResult

public interface ExportBatchTestReport {
    public operator fun invoke(report: Report, format: Format): Result

    public data class Report(
        val runId: String,
        val folderId: String,
        val deviceId: String,
        val deviceName: String,
        val results: List<BatchTestResult>,
    )

    public enum class Format {
        JSON,
        CSV,
    }

    public sealed interface Result {
        public data class Success(val fileName: String) : Result
        public data class Error(val throwable: Throwable) : Result
    }
}
