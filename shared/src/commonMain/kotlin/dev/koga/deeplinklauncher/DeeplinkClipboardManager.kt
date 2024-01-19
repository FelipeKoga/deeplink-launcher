package dev.koga.deeplinklauncher

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

expect class DeeplinkClipboardManager {
    val clipboardText: StateFlow<String?>

    fun copy(text: String)
    fun dismissDeepLink()
}