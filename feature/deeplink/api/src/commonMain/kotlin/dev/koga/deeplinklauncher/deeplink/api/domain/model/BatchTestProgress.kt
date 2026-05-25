package dev.koga.deeplinklauncher.deeplink.api.domain.model

public sealed interface BatchTestProgress {
    public val runId: String

    public data class Started(
        override val runId: String,
        val folderName: String,
        val totalLinks: Int,
        val deviceName: String,
    ) : BatchTestProgress

    public data class LinkStarted(
        override val runId: String,
        val index: Int,
        val total: Int,
        val deepLink: DeepLink,
        val assertion: DeepLinkAssertion?,
    ) : BatchTestProgress

    public data class LaunchCompleted(
        override val runId: String,
        val index: Int,
        val total: Int,
        val deepLink: DeepLink,
        val launchStatus: BatchTestLaunchStatus,
    ) : BatchTestProgress

    public data class AwaitingManualVerification(
        override val runId: String,
        val index: Int,
        val total: Int,
        val deepLink: DeepLink,
        val launchStatus: BatchTestLaunchStatus,
        val verificationStatus: BatchTestVerificationStatus,
        val autoDetails: String?,
        val foregroundPackage: String?,
        val foregroundActivity: String?,
        val capturedTexts: List<String> = emptyList(),
    ) : BatchTestProgress

    public data class LinkCompleted(
        override val runId: String,
        val index: Int,
        val total: Int,
        val deepLink: DeepLink,
        val result: BatchTestResult,
    ) : BatchTestProgress

    public data class Finished(
        override val runId: String,
        val folderId: String,
        val deviceId: String,
        val deviceName: String,
        val passed: Int,
        val failed: Int,
        val skipped: Int,
        val results: List<BatchTestResult>,
    ) : BatchTestProgress

    public data class Error(
        override val runId: String,
        val message: String,
    ) : BatchTestProgress

    public data class Stopped(
        override val runId: String,
        val results: List<BatchTestResult>,
    ) : BatchTestProgress
}
