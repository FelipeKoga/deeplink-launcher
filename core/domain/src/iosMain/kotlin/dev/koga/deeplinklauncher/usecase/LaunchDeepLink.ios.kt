package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.util.ext.currentLocalDateTime
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class LaunchDeepLink(
    private val dataSource: DeepLinkDataSource,
) {
    private val application = UIApplication.sharedApplication

    actual fun launch(url: String): LaunchDeepLinkResult {
        val nsurl = NSURL(string = url)

        return if (application.canOpenURL(nsurl)) {
            application.openURL(nsurl)
            LaunchDeepLinkResult.Success(url)
        } else {
            LaunchDeepLinkResult.Failure(IllegalArgumentException("Cannot open URL"))
        }
    }

    actual fun launch(deepLink: DeepLink): LaunchDeepLinkResult {
        dataSource.upsertDeepLink(deepLink.copy(lastLaunchedAt = currentLocalDateTime))
        return launch(deepLink.link)    }
}
