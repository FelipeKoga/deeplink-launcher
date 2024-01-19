package dev.koga.deeplinklauncher

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

actual class DeeplinkClipboardManager {

    private val clipboardTextStream = MutableStateFlow()
    actual val clipboardText: Flow<String>
        get() = TODO("Not yet implemented")

    actual fun copyToClipboard(text: String) {
    }
}