package dev.koga.deeplinklauncher.deeplink.impl.platform

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor

@SingleIn(AppScope::class)
@Inject
internal actual class ClipboardTextReader {
    actual fun read(): String? {
        try {
            val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
            if (!clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                return null
            }

            return clipboard.getData(DataFlavor.stringFlavor) as? String
        } catch (e: Exception) {
            return null
        }
    }

    actual fun hasTextToPaste(): Boolean = false
}
