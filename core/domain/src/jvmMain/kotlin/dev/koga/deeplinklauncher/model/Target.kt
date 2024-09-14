package dev.koga.deeplinklauncher.model

import dev.koga.deeplinklauncher.provider.UUIDProvider

sealed interface Target {
    val id: String

    data object Browser : Target {
        override val id: String = UUIDProvider.get()
    }

    data class Device(
        override val id: String,
        val name: String,
        val platform: Platform
    ) : Target {
        enum class Platform {
            ANDROID,
            IOS
        }
    }
}
