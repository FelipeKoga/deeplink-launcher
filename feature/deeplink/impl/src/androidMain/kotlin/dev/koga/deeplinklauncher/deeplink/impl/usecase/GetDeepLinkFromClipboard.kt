package dev.koga.deeplinklauncher.deeplink.impl.usecase

import android.content.ClipboardManager
import android.content.Context
import dev.koga.deeplinklauncher.deeplink.api.usecase.ValidateDeepLink

actual class GetDeepLinkFromClipboard(
    private val context: Context,
    private val validateDeepLink: ValidateDeepLink,
) {
    actual operator fun invoke(): String? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        if (!clipboard.hasPrimaryClip()) return null

        val text = clipboard.primaryClip?.getItemAt(0)?.text ?: return null

        if (!validateDeepLink.isValid(text.toString())) return null

        return text.toString()
    }
}