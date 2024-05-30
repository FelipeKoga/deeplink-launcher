package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.DeepLink
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

actual class ShareDeepLink {
    actual operator fun invoke(deepLink: DeepLink) {
        val stringSelection = StringSelection(deepLink.link)
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(stringSelection, null)
    }
}
