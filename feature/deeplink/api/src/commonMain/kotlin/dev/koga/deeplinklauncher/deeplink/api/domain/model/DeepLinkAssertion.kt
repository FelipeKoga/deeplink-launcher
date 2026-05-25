package dev.koga.deeplinklauncher.deeplink.api.domain.model

import kotlinx.serialization.Serializable

@Serializable
public data class DeepLinkAssertion(
    val expectedPackage: String? = null,
    val expectedActivityPattern: String? = null,
    val expectedTexts: List<String> = emptyList(),
    val waitAfterLaunchMs: Long = MIN_WAIT_AFTER_LAUNCH_MS,
) {
    public val hasCriteria: Boolean
        get() = !expectedPackage.isNullOrBlank() ||
            !expectedActivityPattern.isNullOrBlank() ||
            expectedTexts.any { it.isNotBlank() }

    public companion object {
        public const val MIN_WAIT_AFTER_LAUNCH_MS: Long = 10_000L
        public const val MAX_WAIT_AFTER_LAUNCH_MS: Long = 30_000L
    }
}
