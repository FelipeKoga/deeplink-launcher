package dev.koga.deeplinklauncher.deeplink.impl.platform

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import platform.Foundation.NSThread
import platform.UIKit.UIPasteboard
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_sync

@SingleIn(AppScope::class)
@Inject
internal actual class ClipboardTextReader {
    actual fun read(): String? = null

    actual fun hasTextToPaste(): Boolean {
        if (NSThread.isMainThread) return UIPasteboard.generalPasteboard.hasStrings

        var hasStrings = false
        dispatch_sync(dispatch_get_main_queue()) {
            hasStrings = UIPasteboard.generalPasteboard.hasStrings
        }
        return hasStrings
    }
}
