package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestProgress
import dev.koga.deeplinklauncher.deeplink.api.domain.model.BatchTestVerificationStatus
import kotlinx.coroutines.flow.Flow

public interface RunFolderBatchTest {
    public operator fun invoke(folderId: String): Flow<BatchTestProgress>

    public suspend fun submitManualVerification(
        runId: String,
        verificationStatus: BatchTestVerificationStatus,
    )

    public suspend fun stop(runId: String)
}
