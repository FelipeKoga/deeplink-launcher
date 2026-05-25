package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkAssertion

public interface VerifyDeepLinkAssertion {
    public suspend operator fun invoke(
        deviceId: String,
        assertion: DeepLinkAssertion,
    ): Result

    public sealed interface Result {
        public data class Passed(
            val foregroundPackage: String?,
            val foregroundActivity: String?,
            val matchedTexts: List<String>,
        ) : Result

        public data class Failed(
            val reason: String,
            val foregroundPackage: String?,
            val foregroundActivity: String?,
            val missingTexts: List<String> = emptyList(),
        ) : Result
    }
}
