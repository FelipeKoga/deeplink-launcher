package dev.koga.deeplinklauncher.provider

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import dev.koga.deeplinklauncher.usecase.ValidateDeepLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

actual class DeepLinkClipboardProvider(
    private val context: Context,
    private val validateDeepLink: ValidateDeepLink,
) {
    private val clipboardManager
        get() = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    private val dismissedDeepLinks = mutableSetOf<String>()
    private val currentText
        get() = clipboardManager.primaryClip?.let { primaryClip ->
            val text = primaryClip.getItemAt(0)?.text?.toString()

            if (validateDeepLink(text ?: "")) {
                return@let text
            }

            null
        }

    private var inited = false

    private val stream = MutableStateFlow<String?>(null)
    actual val clipboardText = stream.asStateFlow()

    init {
        clipboardManager.addPrimaryClipChangedListener {
            if (currentText in dismissedDeepLinks) return@addPrimaryClipChangedListener

            stream.update { currentText }
        }
    }

    actual fun copy(text: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("DeepLink", text))
    }

    actual fun dismissDeepLink() {
        dismissedDeepLinks.add(clipboardText.value ?: return)
        stream.update { null }
    }

    fun initWithCurrentClipboardText() {
        if (inited) return

        stream.update { currentText }
        inited = true
    }
}
