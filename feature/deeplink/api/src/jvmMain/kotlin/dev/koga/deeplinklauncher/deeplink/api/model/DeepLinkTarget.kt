@file:OptIn(ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.deeplink.api.model

import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
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
        val platform: DeviceBridge.Platform,
    ) : DeepLinkTarget
}
