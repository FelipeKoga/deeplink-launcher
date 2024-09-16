package dev.koga.deeplinklauncher.model

import dev.koga.deeplinklauncher.devicebridge.DeviceBridge
import dev.koga.deeplinklauncher.provider.UUIDProvider

sealed interface DeeplinkTarget {
    val id: String

    data object Browser : DeeplinkTarget {
        override val id: String = UUIDProvider.get()
    }

    data class Device(
        override val id: String,
        val name: String,
        val platform: DeviceBridge.Platform,
    ) : DeeplinkTarget
}
