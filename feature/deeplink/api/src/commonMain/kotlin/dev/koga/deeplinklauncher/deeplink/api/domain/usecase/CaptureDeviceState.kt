package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkAssertion

public interface CaptureDeviceState {
    public suspend operator fun invoke(deviceId: String): Result

    public sealed interface Result {
        public data class Success(
            val assertion: DeepLinkAssertion,
            val visibleTexts: List<String>,
        ) : Result

        public data class Failure(val message: String) : Result
    }
}
