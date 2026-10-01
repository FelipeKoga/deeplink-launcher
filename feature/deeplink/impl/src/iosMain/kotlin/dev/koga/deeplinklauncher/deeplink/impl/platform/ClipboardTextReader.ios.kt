package dev.koga.deeplinklauncher.deeplink.impl.platform

import platform.Foundation.NSThread
import platform.UIKit.UIPasteboard
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_sync

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
