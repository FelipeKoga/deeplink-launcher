package dev.koga.deeplinklauncher.usecase

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

actual class CopyToClipboard(
    private val context: Context
) {
    actual fun copy(label: String, value: String) {
        val clipboard: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText(label, value)
        clipboard.setPrimaryClip(clip)
    }
}