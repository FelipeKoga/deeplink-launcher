package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkTargetStateManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestLaunchStatus
import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestProgress
import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestResult
import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestVerificationStatus
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkAssertion
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkTarget
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.RunFolderBatchTest
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.VerifyDeepLinkAssertion
import dev.koga.deeplinklauncher.deeplink.impl.domain.batchtest.extractVisibleTextsFromUiDump
import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.TimeSource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalUuidApi::class)
internal class RunFolderBatchTestImpl(
    private val folderRepository: FolderRepository,
    private val launchDeepLink: LaunchDeepLink,
    private val verifyDeepLinkAssertion: VerifyDeepLinkAssertion,
    private val deepLinkTargetStateManager: DeepLinkTargetStateManager,
    private val deviceBridge: DeviceBridge,
) : RunFolderBatchTest {

    private val manualVerifications = ConcurrentHashMap<String, CompletableDeferred<BatchTestVerificationStatus>>()
    private val stopRequests = ConcurrentHashMap<String, CompletableDeferred<Unit>>()
    private val activeVerificationKey = AtomicReference<String?>(null)

    override fun invoke(folderId: String): Flow<BatchTestProgress> = flow {
        val runId = Uuid.random().toString()
        val target = deepLinkTargetStateManager.current.value
        val device = target as? DeepLinkTarget.Device
        if (device == null) {
            emit(BatchTestProgress.Error(runId, "Select an Android device before running batch tests"))
            return@flow
        }
        if (device.platform != DeviceBridge.Platform.ANDROID) {
            emit(
                BatchTestProgress.Error(
                    runId,
                    "Batch test automation requires an Android device connected via ADB",
                ),
            )
            return@flow
        }

        val folder = folderRepository.getFolderById(folderId)
        if (folder == null) {
            emit(BatchTestProgress.Error(runId, "Folder not found"))
            return@flow
        }

        val deepLinks = folderRepository.getFolderDeepLinks(folderId)
        if (deepLinks.isEmpty()) {
            emit(BatchTestProgress.Error(runId, "This folder has no deeplinks to test"))
            return@flow
        }

        stopRequests[runId] = CompletableDeferred()

        emit(
            BatchTestProgress.Started(
                runId = runId,
                folderName = folder.name,
                totalLinks = deepLinks.size,
                deviceName = device.name,
            ),
        )

        val results = mutableListOf<BatchTestResult>()
        var passed = 0
        var failed = 0
        var skipped = 0

        deepLinks.forEachIndexed { index, deepLink ->
            if (stopRequests[runId]?.isCompleted == true) {
                emit(BatchTestProgress.Stopped(runId, results.toList()))
                cleanup(runId)
                return@flow
            }

            emit(
                BatchTestProgress.LinkStarted(
                    runId = runId,
                    index = index,
                    total = deepLinks.size,
                    deepLink = deepLink,
                    assertion = deepLink.assertion,
                ),
            )

            val linkStartedAt = TimeSource.Monotonic.markNow()
            val launchStatus = performLaunch(deepLink)
            emit(
                BatchTestProgress.LaunchCompleted(
                    runId = runId,
                    index = index,
                    total = deepLinks.size,
                    deepLink = deepLink,
                    launchStatus = launchStatus,
                ),
            )

            val resolution = resolveVerificationStatus(
                runId = runId,
                index = index,
                total = deepLinks.size,
                deviceId = device.id,
                deepLink = deepLink,
                launchStatus = launchStatus,
            ) { progress ->
                emit(progress)
            }

            when (resolution.status) {
                is BatchTestVerificationStatus.AutoPassed,
                is BatchTestVerificationStatus.ManualPassed,
                -> passed++

                is BatchTestVerificationStatus.Skipped -> skipped++
                else -> failed++
            }

            val result = BatchTestResult(
                id = Uuid.random().toString(),
                runId = runId,
                deepLinkId = deepLink.id,
                link = deepLink.link,
                launchStatus = launchStatus,
                verificationStatus = resolution.status,
                autoDetails = resolution.autoDetails,
                foregroundPackage = resolution.foregroundPackage,
                foregroundActivity = resolution.foregroundActivity,
                durationMs = linkStartedAt.elapsedNow().inWholeMilliseconds,
            )
            results += result

            emit(
                BatchTestProgress.LinkCompleted(
                    runId = runId,
                    index = index,
                    total = deepLinks.size,
                    deepLink = deepLink,
                    result = result,
                ),
            )
        }

        cleanup(runId)
        emit(
            BatchTestProgress.Finished(
                runId = runId,
                folderId = folderId,
                deviceId = device.id,
                deviceName = device.name,
                passed = passed,
                failed = failed,
                skipped = skipped,
                results = results.toList(),
            ),
        )
    }

    override suspend fun submitManualVerification(
        runId: String,
        verificationStatus: BatchTestVerificationStatus,
    ) {
        activeVerificationKey.get()?.let { key ->
            manualVerifications[key]?.complete(verificationStatus)
        }
    }

    override suspend fun stop(runId: String) {
        stopRequests[runId]?.complete(Unit)
        activeVerificationKey.get()?.let { key ->
            manualVerifications[key]?.complete(BatchTestVerificationStatus.Skipped)
        }
    }

    private suspend fun resolveVerificationStatus(
        runId: String,
        index: Int,
        total: Int,
        deviceId: String,
        deepLink: DeepLink,
        launchStatus: BatchTestLaunchStatus,
        emitProgress: suspend (BatchTestProgress) -> Unit,
    ): VerificationResolution {
        if (launchStatus is BatchTestLaunchStatus.Failure) {
            return VerificationResolution(
                status = BatchTestVerificationStatus.ManualFailed,
                autoDetails = launchStatus.message,
            )
        }

        val assertion = deepLink.assertion
        if (assertion?.hasCriteria == true) {
            when (val verification = verifyDeepLinkAssertion(deviceId, assertion)) {
                is VerifyDeepLinkAssertion.Result.Passed -> {
                    return VerificationResolution(
                        status = BatchTestVerificationStatus.AutoPassed,
                        foregroundPackage = verification.foregroundPackage,
                        foregroundActivity = verification.foregroundActivity,
                    )
                }

                is VerifyDeepLinkAssertion.Result.Failed -> {
                    val evidence = captureEvidence(deviceId)
                    val pendingStatus = BatchTestVerificationStatus.AutoFailed(verification.reason)
                    val manualStatus = awaitManualVerification(
                        runId = runId,
                        index = index,
                        total = total,
                        deepLink = deepLink,
                        launchStatus = launchStatus,
                        verificationStatus = pendingStatus,
                        autoDetails = verification.reason,
                        foregroundPackage = verification.foregroundPackage ?: evidence?.foregroundPackage,
                        foregroundActivity = verification.foregroundActivity ?: evidence?.foregroundActivity,
                        capturedTexts = evidence?.capturedTexts.orEmpty(),
                        emitProgress = emitProgress,
                    )
                    return VerificationResolution(
                        status = manualStatus,
                        autoDetails = verification.reason,
                        foregroundPackage = verification.foregroundPackage ?: evidence?.foregroundPackage,
                        foregroundActivity = verification.foregroundActivity ?: evidence?.foregroundActivity,
                    )
                }
            }
        }

        waitAfterLaunch(deepLink)
        val evidence = captureEvidence(deviceId)
        val manualStatus = awaitManualVerification(
            runId = runId,
            index = index,
            total = total,
            deepLink = deepLink,
            launchStatus = launchStatus,
            verificationStatus = BatchTestVerificationStatus.NotVerified,
            autoDetails = null,
            foregroundPackage = evidence?.foregroundPackage,
            foregroundActivity = evidence?.foregroundActivity,
            capturedTexts = evidence?.capturedTexts.orEmpty(),
            emitProgress = emitProgress,
        )
        return VerificationResolution(
            status = manualStatus,
            foregroundPackage = evidence?.foregroundPackage,
            foregroundActivity = evidence?.foregroundActivity,
        )
    }

    private suspend fun awaitManualVerification(
        runId: String,
        index: Int,
        total: Int,
        deepLink: DeepLink,
        launchStatus: BatchTestLaunchStatus,
        verificationStatus: BatchTestVerificationStatus,
        autoDetails: String?,
        foregroundPackage: String?,
        foregroundActivity: String?,
        capturedTexts: List<String>,
        emitProgress: suspend (BatchTestProgress) -> Unit,
    ): BatchTestVerificationStatus {
        val verificationKey = "$runId:$index"
        val deferred = CompletableDeferred<BatchTestVerificationStatus>()
        manualVerifications[verificationKey] = deferred
        activeVerificationKey.set(verificationKey)

        emitProgress(
            BatchTestProgress.AwaitingManualVerification(
                runId = runId,
                index = index,
                total = total,
                deepLink = deepLink,
                launchStatus = launchStatus,
                verificationStatus = verificationStatus,
                autoDetails = autoDetails,
                foregroundPackage = foregroundPackage,
                foregroundActivity = foregroundActivity,
                capturedTexts = capturedTexts,
            ),
        )

        return deferred.await().also {
            manualVerifications.remove(verificationKey)
            if (activeVerificationKey.get() == verificationKey) {
                activeVerificationKey.set(null)
            }
        }
    }

    private suspend fun captureEvidence(deviceId: String): VerificationEvidence? {
        val foreground = deviceBridge.getForegroundActivity(deviceId) ?: return null
        val uiDump = deviceBridge.dumpUiHierarchy(deviceId)
        return VerificationEvidence(
            foregroundPackage = foreground.packageName,
            foregroundActivity = foreground.activityClass,
            capturedTexts = extractVisibleTextsFromUiDump(uiDump).take(10),
        )
    }

    private suspend fun waitAfterLaunch(deepLink: DeepLink) {
        val waitMs = (deepLink.assertion?.waitAfterLaunchMs ?: DeepLinkAssertion.MIN_WAIT_AFTER_LAUNCH_MS)
            .coerceIn(
                DeepLinkAssertion.MIN_WAIT_AFTER_LAUNCH_MS,
                DeepLinkAssertion.MAX_WAIT_AFTER_LAUNCH_MS,
            )
        delay(waitMs)
    }

    private suspend fun performLaunch(deepLink: DeepLink): BatchTestLaunchStatus {
        return when (val result = launchDeepLink.launch(deepLink)) {
            is LaunchDeepLink.Result.Success -> BatchTestLaunchStatus.Success
            is LaunchDeepLink.Result.Failure -> BatchTestLaunchStatus.Failure(
                result.throwable.message ?: "Launch failed",
            )
        }
    }

    private fun cleanup(runId: String) {
        stopRequests.remove(runId)
        activeVerificationKey.set(null)
    }

    private data class VerificationEvidence(
        val foregroundPackage: String?,
        val foregroundActivity: String?,
        val capturedTexts: List<String> = emptyList(),
    )

    private data class VerificationResolution(
        val status: BatchTestVerificationStatus,
        val autoDetails: String? = null,
        val foregroundPackage: String? = null,
        val foregroundActivity: String? = null,
    )
}
