package dev.koga.deeplinklauncher.deeplink.impl.platform

import android.content.ClipboardManager
import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@Inject
internal actual class ClipboardTextReader(
    private val context: Context,
) {
    actual fun read(): String? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        if (!clipboard.hasPrimaryClip()) return null

        return clipboard.primaryClip?.getItemAt(0)?.text?.toString()
    }

    actual fun hasTextToPaste(): Boolean = false
}
