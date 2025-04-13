package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.usecase.ValidateDeepLink
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor

actual class GetDeepLinkFromClipboard(
    private val validateDeepLink: ValidateDeepLink,
) {
    private val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard

    actual operator fun invoke(): String? {
        try {
            if (!clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                return null
            }

            val text = clipboard.getData(DataFlavor.stringFlavor) as? String ?: return null

            if (!validateDeepLink.isValid(text)) return null

            return text
        } catch (e: Exception) {
            return null
        }
    }
}
