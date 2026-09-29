package dev.koga.deeplinklauncher.deeplink.impl.platform

import platform.UIKit.UIPasteboard

internal actual class ClipboardTextReader {
    actual fun read(): String? = null

    actual fun hasTextToPaste(): Boolean = UIPasteboard.generalPasteboard.hasStrings
}
