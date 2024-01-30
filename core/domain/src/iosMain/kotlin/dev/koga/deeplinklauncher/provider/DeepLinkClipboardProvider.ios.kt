package dev.koga.deeplinklauncher.provider

import kotlinx.coroutines.flow.StateFlow

actual class DeepLinkClipboardProvider {
    actual val clipboardText: StateFlow<String?>
        get() = TODO("Not yet implemented")

    actual fun copy(text: String) {
    }

    actual fun dismissDeepLink() {
    }
}
