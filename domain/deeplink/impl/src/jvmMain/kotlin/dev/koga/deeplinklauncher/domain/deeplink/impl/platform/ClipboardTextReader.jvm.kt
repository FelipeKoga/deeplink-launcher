package dev.koga.deeplinklauncher.domain.deeplink.impl.platform
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor

internal actual class ClipboardTextReader {
    private val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard

    actual fun read(): String? {
        try {
            if (!clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                return null
            }

            return clipboard.getData(DataFlavor.stringFlavor) as? String
        } catch (e: Exception) {
            return null
        }
    }
}
