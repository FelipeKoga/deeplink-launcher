package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.ShareDeepLink
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

internal class ShareDeepLinkImpl : ShareDeepLink {
    override operator fun invoke(deepLink: DeepLink) {
        val stringSelection = StringSelection(deepLink.link)
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(stringSelection, null)
    }
}
