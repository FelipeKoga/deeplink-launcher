package dev.koga.deeplinklauncher.deeplink.api.domain.model

public data class BatchTestResult(
    val id: String,
    val runId: String,
    val deepLinkId: String,
    val link: String,
    val launchStatus: BatchTestLaunchStatus,
    val verificationStatus: BatchTestVerificationStatus,
    val autoDetails: String? = null,
    val foregroundPackage: String? = null,
    val foregroundActivity: String? = null,
    val durationMs: Long,
)

public sealed interface BatchTestLaunchStatus {
    public data object Success : BatchTestLaunchStatus
    public data class Failure(val message: String) : BatchTestLaunchStatus
}

public sealed interface BatchTestVerificationStatus {
    public data object AutoPassed : BatchTestVerificationStatus
    public data class AutoFailed(val reason: String) : BatchTestVerificationStatus
    public data object ManualPassed : BatchTestVerificationStatus
    public data object ManualFailed : BatchTestVerificationStatus
    public data object Skipped : BatchTestVerificationStatus
    public data object NotVerified : BatchTestVerificationStatus

    public val isPassed: Boolean
        get() = this is AutoPassed || this is ManualPassed
}
