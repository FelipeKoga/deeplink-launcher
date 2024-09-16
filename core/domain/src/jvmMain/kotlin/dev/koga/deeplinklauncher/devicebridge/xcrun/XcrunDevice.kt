package dev.koga.deeplinklauncher.devicebridge.xcrun

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class XcrunDevice(
    @SerialName("udid") val udid: String,
    @SerialName("state") val state: String,
    @SerialName("name") val name: String,
)