package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestLaunchStatus
import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestVerificationStatus
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ExportBatchTestReport
import dev.koga.deeplinklauncher.file.SaveFile
import dev.koga.deeplinklauncher.file.model.FileType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class ExportBatchTestReportImpl(
    private val saveFile: SaveFile,
) : ExportBatchTestReport {

    override fun invoke(report: ExportBatchTestReport.Report, format: ExportBatchTestReport.Format): ExportBatchTestReport.Result {
        return try {
            val sanitizedTimestamp = currentLocalDateTime.toString().replace(':', '_')
            val content = when (format) {
                ExportBatchTestReport.Format.JSON -> buildJsonReport(report)
                ExportBatchTestReport.Format.CSV -> buildCsvReport(report)
            }
            val extension = when (format) {
                ExportBatchTestReport.Format.JSON -> FileType.JSON.extension
                ExportBatchTestReport.Format.CSV -> "csv"
            }
            val fileName = "batch-test-$sanitizedTimestamp.$extension"
            val fileType = when (format) {
                ExportBatchTestReport.Format.JSON -> FileType.JSON
                ExportBatchTestReport.Format.CSV -> FileType.TXT
            }

            val filePath = saveFile(
                fileName = fileName,
                fileContent = content,
                type = fileType,
            ) ?: return ExportBatchTestReport.Result.Error(Exception("Failed to save report"))

            ExportBatchTestReport.Result.Success(fileName = filePath.substringAfterLast('/'))
        } catch (throwable: Throwable) {
            ExportBatchTestReport.Result.Error(throwable)
        }
    }

    private fun buildJsonReport(report: ExportBatchTestReport.Report): String {
        val payload = BatchTestReportPayload(
            run = BatchTestReportPayload.Run(
                id = report.runId,
                folderId = report.folderId,
                deviceId = report.deviceId,
                deviceName = report.deviceName,
            ),
            results = report.results.map { result ->
                BatchTestReportPayload.Result(
                    deepLinkId = result.deepLinkId,
                    link = result.link,
                    launchStatus = result.launchStatus.toReportValue(),
                    launchError = (result.launchStatus as? BatchTestLaunchStatus.Failure)?.message,
                    verificationStatus = result.verificationStatus.toReportValue(),
                    autoDetails = result.autoDetails,
                    foregroundPackage = result.foregroundPackage,
                    foregroundActivity = result.foregroundActivity,
                    durationMs = result.durationMs,
                    passed = result.verificationStatus.isPassed,
                )
            },
        )
        return json.encodeToString(payload)
    }

    private fun buildCsvReport(report: ExportBatchTestReport.Report): String {
        val header = listOf(
            "link",
            "launch_status",
            "launch_error",
            "verification_status",
            "auto_details",
            "foreground_package",
            "foreground_activity",
            "duration_ms",
            "passed",
        ).joinToString(",")

        val rows = report.results.map { result ->
            listOf(
                result.link.escapeCsv(),
                result.launchStatus.toReportValue().escapeCsv(),
                ((result.launchStatus as? BatchTestLaunchStatus.Failure)?.message).orEmpty().escapeCsv(),
                result.verificationStatus.toReportValue().escapeCsv(),
                result.autoDetails.orEmpty().escapeCsv(),
                result.foregroundPackage.orEmpty().escapeCsv(),
                result.foregroundActivity.orEmpty().escapeCsv(),
                result.durationMs.toString(),
                result.verificationStatus.isPassed.toString(),
            ).joinToString(",")
        }

        return buildString {
            appendLine("run_id,${report.runId.escapeCsv()}")
            appendLine("device_name,${report.deviceName.escapeCsv()}")
            appendLine(header)
            rows.forEach { appendLine(it) }
        }
    }

    private fun BatchTestLaunchStatus.toReportValue(): String = when (this) {
        BatchTestLaunchStatus.Success -> "success"
        is BatchTestLaunchStatus.Failure -> "failure"
    }

    private fun BatchTestVerificationStatus.toReportValue(): String = when (this) {
        BatchTestVerificationStatus.AutoPassed -> "auto_passed"
        is BatchTestVerificationStatus.AutoFailed -> "auto_failed"
        BatchTestVerificationStatus.ManualPassed -> "manual_passed"
        BatchTestVerificationStatus.ManualFailed -> "manual_failed"
        BatchTestVerificationStatus.Skipped -> "skipped"
        BatchTestVerificationStatus.NotVerified -> "not_verified"
    }

    private fun String.escapeCsv(): String {
        return if (contains(',') || contains('"') || contains('\n')) {
            "\"${replace("\"", "\"\"")}\""
        } else {
            this
        }
    }

    @Serializable
    private data class BatchTestReportPayload(
        val run: Run,
        val results: List<Result>,
    ) {
        @Serializable
        data class Run(
            val id: String,
            val folderId: String,
            val deviceId: String,
            val deviceName: String,
        )

        @Serializable
        data class Result(
            val deepLinkId: String,
            val link: String,
            val launchStatus: String,
            val launchError: String?,
            val verificationStatus: String,
            val autoDetails: String?,
            val foregroundPackage: String?,
            val foregroundActivity: String?,
            val durationMs: Long,
            val passed: Boolean,
        )
    }

    private companion object {
        private val json = Json {
            prettyPrint = true
            encodeDefaults = true
        }
    }
}
