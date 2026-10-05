package dev.koga.deeplinklauncher.cli.output

import dev.koga.deeplinklauncher.cli.device.Device
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal const val SCHEMA_VERSION = 1

@Serializable
internal data class ErrorReport(
    val schemaVersion: Int = SCHEMA_VERSION,
    val command: String,
    val error: ErrorBody,
) {
    @Serializable
    data class ErrorBody(val exitCode: Int, val message: String, val hint: String? = null)
}

@Serializable
internal data class DevicesReport(
    val schemaVersion: Int = SCHEMA_VERSION,
    val command: String = "devices",
    val devices: List<Device>,
)

@Serializable
internal data class ParseReport(
    val schemaVersion: Int = SCHEMA_VERSION,
    val command: String = "parse",
    val text: String,
    val valid: Boolean,
    val looksLikeDeepLink: Boolean,
    val scheme: String?,
    val host: String?,
    val path: String,
    val query: String?,
    val fragment: String?,
)

@Serializable
internal enum class ResolveStatus {
    @SerialName("handled")
    HANDLED,

    @SerialName("unhandled")
    UNHANDLED,

    @SerialName("unknown")
    UNKNOWN,
}

@Serializable
internal data class Handler(val id: String, val name: String? = null)

@Serializable
internal data class ResolveReport(
    val schemaVersion: Int = SCHEMA_VERSION,
    val command: String = "resolve",
    val url: String,
    val device: Device,
    val status: ResolveStatus,
    val defaultHandler: Handler?,
    val handlers: List<Handler>,
    val note: String? = null,
)

@Serializable
internal enum class OpenStatus {
    @SerialName("opened")
    OPENED,

    @SerialName("unhandled")
    UNHANDLED,

    @SerialName("crashed")
    CRASHED,
}

@Serializable
internal data class OpenReport(
    val schemaVersion: Int = SCHEMA_VERSION,
    val command: String = "open",
    val url: String,
    val device: Device,
    val status: OpenStatus,
    val handler: Handler?,
    val launch: String? = null,
    val timeMs: Long? = null,
    val alive: Boolean? = null,
    val watchMs: Long,
    val crash: String? = null,
    val note: String? = null,
)

@Serializable
internal data class DoctorReport(
    val schemaVersion: Int = SCHEMA_VERSION,
    val command: String = "doctor",
    val ready: Boolean,
    val checks: List<Check>,
) {
    @Serializable
    data class Check(val name: String, val ok: Boolean, val detail: String, val fix: String? = null)
}

@Serializable
internal enum class TestStatus {
    @SerialName("passed")
    PASSED,

    @SerialName("failed")
    FAILED,

    @SerialName("unverified")
    UNVERIFIED,
}

@Serializable
internal data class TestReport(
    val schemaVersion: Int = SCHEMA_VERSION,
    val command: String = "test",
    val device: Device,
    val summary: Summary,
    val results: List<CaseResult>,
) {
    @Serializable
    data class Summary(val total: Int, val passed: Int, val failed: Int, val unverified: Int)

    @Serializable
    data class CaseResult(
        val name: String?,
        val url: String,
        val folder: String?,
        val status: TestStatus,
        val reason: String?,
        val expected: Expected,
        val actual: Actual,
    )

    @Serializable
    data class Expected(val opens: Boolean, val handler: String?)

    @Serializable
    data class Actual(val status: OpenStatus, val handler: String?, val timeMs: Long?, val crash: String?)
}
