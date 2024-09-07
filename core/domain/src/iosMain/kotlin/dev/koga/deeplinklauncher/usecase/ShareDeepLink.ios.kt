package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.DeepLink
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

actual class ShareDeepLink {
    actual operator fun invoke(deepLink: DeepLink) {
        val activityViewController = UIActivityViewController(
            activityItems = listOf(deepLink.link),
            applicationActivities = null,
        )

        // Access the root view controller and present the share sheet
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(
            activityViewController,
            animated = true,
            completion = null,
        )
    }
}
