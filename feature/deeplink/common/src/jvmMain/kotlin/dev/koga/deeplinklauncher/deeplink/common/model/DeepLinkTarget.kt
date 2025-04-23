@file:OptIn(ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.deeplink.common.model

import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface DeepLinkTarget {
    val id: String

    data object Desktop : DeepLinkTarget {
        override val id: String = Uuid.random().toString()
    }

    data class Device(
        override val id: String,
        val name: String,
        val platform: DeviceBridge.Platform,
    ) : DeepLinkTarget
}
