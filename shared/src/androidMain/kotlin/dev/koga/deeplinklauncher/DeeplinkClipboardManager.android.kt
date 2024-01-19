package dev.koga.deeplinklauncher

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

actual class DeeplinkClipboardManager(
    private val context: Context
) {
    private val manager get() = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

    private val dismissedDeepLinks = mutableSetOf<String>()
    private val currentText
        get() = manager.primaryClip?.getItemAt(0)?.text?.toString()

    private val stream = MutableStateFlow(currentText)

    actual val clipboardText = stream.asStateFlow()

    init {
        manager.addPrimaryClipChangedListener {
            if (currentText in dismissedDeepLinks) return@addPrimaryClipChangedListener

            stream.update { currentText }
        }
    }

    actual fun copy(text: String) {
        manager.setPrimaryClip(ClipData.newPlainText("DeepLink", text))
    }

    actual fun dismissDeepLink() {
        dismissedDeepLinks.add(clipboardText.value ?: return)
        stream.update { null }
    }
}