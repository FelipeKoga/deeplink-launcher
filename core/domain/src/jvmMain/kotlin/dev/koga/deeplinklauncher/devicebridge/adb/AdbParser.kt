package dev.koga.deeplinklauncher.devicebridge.adb

import java.io.InputStream
import java.io.Reader

internal object AdbParser {

    suspend fun parse(
        inputStream: InputStream,
        block: suspend (AdbDevice) -> Unit,
    ) {
        return inputStream.bufferedReader()
            .process(regex = Regex(pattern = "(device)\\s*(\\{[^}]+})")) {
                val (_, data) = it.destructured
                val contentRegex = Regex(pattern = "([^{\\s]+)\\s*:\\s*\"?([^\"\\s}]+)")

                val contentData = mutableMapOf<String, String>()
                contentRegex.findAll(data).forEach { match ->
                    val (key, value) = match.destructured
                    contentData[key] = value
                }

                val connectionType = contentData["connection_type"].toString()
                val serial = contentData["serial"].toString()
                val state = contentData["state"].toString()

                block(
                    AdbDevice(
                        serial = serial,
                        connectionType = connectionType,
                        state = state,
                    ),
                )
            }
    }

    private suspend inline fun Reader.process(
        regex: Regex,
        crossinline block: suspend (MatchResult) -> Unit,
    ) {
        val builder = StringBuilder()

        return useLines { lines ->
            lines.forEach { line ->
                builder.append(line)
                regex.find(builder)?.let {
                    block(it)
                    builder.clear()
                }
            }
        }
    }
}
