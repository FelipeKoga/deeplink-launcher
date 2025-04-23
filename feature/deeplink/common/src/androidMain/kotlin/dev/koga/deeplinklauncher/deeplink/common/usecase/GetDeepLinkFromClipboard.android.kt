package dev.koga.deeplinklauncher.deeplink.common.usecase

import android.content.ClipboardManager
import android.content.Context

public class GetDeepLinkFromClipboardImpl(
    private val context: Context,
    private val validateDeepLink: ValidateDeepLink,
) : GetDeepLinkFromClipboard {
    public override operator fun invoke(): String? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        if (!clipboard.hasPrimaryClip()) return null

        val text = clipboard.primaryClip?.getItemAt(0)?.text ?: return null

        if (!validateDeepLink.isValid(text.toString())) return null

        return text.toString()
    }
}
