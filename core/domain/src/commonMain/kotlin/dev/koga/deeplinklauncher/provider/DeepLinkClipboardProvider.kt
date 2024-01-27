package dev.koga.deeplinklauncher.provider

import kotlinx.coroutines.flow.StateFlow

expect class DeepLinkClipboardProvider {
    val clipboardText: StateFlow<String?>

    fun copy(text: String)
    fun dismissDeepLink()
}