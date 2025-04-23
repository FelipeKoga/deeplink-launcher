package dev.koga.deeplinklauncher.deeplink.common.usecase

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor

internal class GetDeepLinkFromClipboardImpl(
    private val validateDeepLink: ValidateDeepLink,
) : GetDeepLinkFromClipboard {
    private val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard

    override operator fun invoke(): String? {
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