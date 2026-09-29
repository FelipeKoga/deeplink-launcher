package dev.koga.deeplinklauncher.deeplink.impl.platform

internal expect class ClipboardTextReader {
    fun read(): String?
    fun hasTextToPaste(): Boolean
}
