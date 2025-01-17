@file:OptIn(ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.devicebridge.model

import dev.koga.deeplinklauncher.devicebridge.DeviceBridge
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface DeeplinkTarget {
    val id: String

    data object Desktop : DeeplinkTarget {
        override val id: String = Uuid.random().toString()
    }

    data class Device(
        override val id: String,
        val name: String,
        val platform: DeviceBridge.Platform,
    ) : DeeplinkTarget
}
