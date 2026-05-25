package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkAssertion
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.VerifyDeepLinkAssertion
import dev.koga.deeplinklauncher.deeplink.impl.domain.batchtest.extractVisibleTextsFromUiDump
import dev.koga.deeplinklauncher.deeplink.impl.domain.batchtest.matchAssertionTexts
import dev.koga.deeplinklauncher.deeplink.impl.domain.batchtest.matchesActivityPattern
import dev.koga.deeplinklauncher.deeplink.impl.domain.batchtest.sanitize
import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
import kotlinx.coroutines.delay

internal class VerifyDeepLinkAssertionImpl(
    private val deviceBridge: DeviceBridge,
) : VerifyDeepLinkAssertion {

    override suspend fun invoke(
        deviceId: String,
        assertion: DeepLinkAssertion,
    ): VerifyDeepLinkAssertion.Result {
        val criteria = assertion.sanitize()
        if (!criteria.hasCriteria) {
            return VerifyDeepLinkAssertion.Result.Failed(
                reason = "No verification criteria configured",
                foregroundPackage = null,
                foregroundActivity = null,
            )
        }

        repeat(MAX_ATTEMPTS) { attempt ->
            delay(if (attempt == 0) criteria.waitAfterLaunchMs else RETRY_DELAY_MS)

            val foreground = deviceBridge.getForegroundActivity(deviceId)
            val uiDump = deviceBridge.dumpUiHierarchy(deviceId)
            val packageName = foreground?.packageName
            val activityClass = foreground?.activityClass

            criteria.expectedPackage?.let { expectedPackage ->
                if (packageName != expectedPackage) {
                    if (attempt == MAX_ATTEMPTS - 1) {
                        return VerifyDeepLinkAssertion.Result.Failed(
                            reason = "Expected package $expectedPackage but found ${packageName ?: "unknown"}",
                            foregroundPackage = packageName,
                            foregroundActivity = activityClass,
                        )
                    }
                    return@repeat
                }
            }

            criteria.expectedActivityPattern?.let { pattern ->
                val activity = activityClass.orEmpty()
                if (!matchesActivityPattern(activity, pattern)) {
                    if (attempt == MAX_ATTEMPTS - 1) {
                        return VerifyDeepLinkAssertion.Result.Failed(
                            reason = "Activity '$activity' does not match pattern '$pattern'",
                            foregroundPackage = packageName,
                            foregroundActivity = activityClass,
                        )
                    }
                    return@repeat
                }
            }

            if (criteria.expectedTexts.isNotEmpty()) {
                val (_, missingTexts) = matchAssertionTexts(uiDump, criteria.expectedTexts)
                if (missingTexts.isNotEmpty()) {
                    if (attempt == MAX_ATTEMPTS - 1) {
                        return VerifyDeepLinkAssertion.Result.Failed(
                            reason = "Missing text on screen: ${missingTexts.joinToString(", ")}",
                            foregroundPackage = packageName,
                            foregroundActivity = activityClass,
                            missingTexts = missingTexts,
                        )
                    }
                    return@repeat
                }
            }

            val matchedTexts = if (criteria.expectedTexts.isEmpty()) {
                emptyList()
            } else {
                matchAssertionTexts(uiDump, criteria.expectedTexts).first
            }

            return VerifyDeepLinkAssertion.Result.Passed(
                foregroundPackage = packageName,
                foregroundActivity = activityClass,
                matchedTexts = matchedTexts,
            )
        }

        return VerifyDeepLinkAssertion.Result.Failed(
            reason = "Verification failed after $MAX_ATTEMPTS attempts",
            foregroundPackage = null,
            foregroundActivity = null,
        )
    }

    private companion object {
        private const val MAX_ATTEMPTS = 3
        private const val RETRY_DELAY_MS = 1000L
    }
}

internal class CaptureDeviceStateImpl(
    private val deviceBridge: DeviceBridge,
) : dev.koga.deeplinklauncher.deeplink.api.domain.usecase.CaptureDeviceState {

    override suspend fun invoke(deviceId: String): dev.koga.deeplinklauncher.deeplink.api.domain.usecase.CaptureDeviceState.Result {
        val foreground = deviceBridge.getForegroundActivity(deviceId)
            ?: return dev.koga.deeplinklauncher.deeplink.api.domain.usecase.CaptureDeviceState.Result.Failure(
                message = "Could not detect foreground activity",
            )

        val uiDump = deviceBridge.dumpUiHierarchy(deviceId)
        val visibleTexts = extractVisibleTextsFromUiDump(uiDump)
            .filter { it.length in 2..80 }
            .take(5)

        return dev.koga.deeplinklauncher.deeplink.api.domain.usecase.CaptureDeviceState.Result.Success(
            assertion = DeepLinkAssertion(
                expectedPackage = foreground.packageName,
                expectedActivityPattern = Regex.escape(foreground.activityClass),
                expectedTexts = visibleTexts,
            ),
            visibleTexts = visibleTexts,
        )
    }
}
