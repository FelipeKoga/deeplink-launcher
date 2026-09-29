package dev.koga.deeplinklauncher.devicebridge.impl.xcrun

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
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

        if (jsonString.isBlank()) return emptyList()

        return try {
            json.decodeFromString<DevicesResponse>(jsonString)
                .devices
                .values
                .flatten()
        } catch (_: SerializationException) {
            emptyList()
        }
    }

    @Serializable
    private data class DevicesResponse(
        @SerialName("devices") val devices: Map<String, List<XcrunDevice>>,
    )
}
