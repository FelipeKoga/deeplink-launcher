package dev.koga.deeplinklauncher.cli.device

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal enum class Platform(val label: String) {
    @SerialName("android")
    ANDROID("Android"),

    @SerialName("ios")
    IOS("iOS"),
}

@Serializable
internal data class Device(
    val id: String,
    val name: String,
    val platform: Platform,
    val virtual: Boolean,
    val osVersion: String? = null,
) {
    val summary: String
        get() = listOfNotNull(id, osVersion?.let { "${platform.label} $it" } ?: platform.label).joinToString(", ")
}
