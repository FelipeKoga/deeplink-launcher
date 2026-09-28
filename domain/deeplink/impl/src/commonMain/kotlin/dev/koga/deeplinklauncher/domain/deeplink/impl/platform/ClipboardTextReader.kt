package dev.koga.deeplinklauncher.domain.deeplink.impl.platform
internal expect class ClipboardTextReader {
    fun read(): String?
}
