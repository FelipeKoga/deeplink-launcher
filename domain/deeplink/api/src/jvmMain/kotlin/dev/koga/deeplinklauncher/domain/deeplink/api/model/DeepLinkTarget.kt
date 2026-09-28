@file:OptIn(ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.domain.deeplink.api.model
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public sealed interface DeepLinkTarget {
    public val id: String

    public data object Desktop : DeepLinkTarget {
        override val id: String = Uuid.random().toString()
    }

    public data class Device(
        override val id: String,
        val name: String,
        val platform: Platform,
    ) : DeepLinkTarget

    /** Device platform, owned by the domain so consumers never see device-bridge types. */
    public enum class Platform { ANDROID, IOS }
}
