package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ShareDeepLink
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
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
