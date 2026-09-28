package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

internal class ShareDeepLinkImpl : ShareDeepLink {
    override fun invoke(deepLink: DeepLink) {
        val activityViewController = UIActivityViewController(
            activityItems = listOf(deepLink.link),
            applicationActivities = null,
        )

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(
            activityViewController,
            animated = true,
            completion = null,
        )
    }
}
