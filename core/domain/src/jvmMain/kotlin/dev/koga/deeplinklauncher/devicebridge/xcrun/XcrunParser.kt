package dev.koga.deeplinklauncher.devicebridge.xcrun

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream

internal object XcrunParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(
        inputStream: InputStream,
    ): List<XcrunDevice> {
        val jsonString = inputStream
            .bufferedReader()
            .use { it.readText() }

        return json.decodeFromString<DevicesResponse>(jsonString)
            .devices
            .values
            .flatten()
    }

    @Serializable
    private data class DevicesResponse(
        @SerialName("devices") val devices: Map<String, List<XcrunDevice>>,
    )
}
