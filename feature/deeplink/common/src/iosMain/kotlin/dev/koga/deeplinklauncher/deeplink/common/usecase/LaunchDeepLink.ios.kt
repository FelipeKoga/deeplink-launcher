package dev.koga.deeplinklauncher.deeplink.common.usecase

import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.common.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.common.repository.DeepLinkRepository
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

public class LaunchDeepLinkImpl(
    private val repository: DeepLinkRepository,
) : LaunchDeepLink {

    private val application = UIApplication.sharedApplication

    override suspend fun launch(url: String): LaunchDeepLink.Result {
        val nsurl = NSURL(string = url)

        return if (application.canOpenURL(nsurl)) {
            application.openURL(nsurl)
            LaunchDeepLink.Result.Success(url)
        } else {
            LaunchDeepLink.Result.Failure(IllegalArgumentException("Cannot open URL"))
        }
    }

    override suspend fun launch(deepLink: DeepLink): LaunchDeepLink.Result {
        repository.upsertDeepLink(deepLink.copy(lastLaunchedAt = currentLocalDateTime))
        return launch(deepLink.link)
    }
}
