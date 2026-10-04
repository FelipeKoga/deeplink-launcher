package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LaunchDeepLink
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class LaunchDeepLinkImpl(
    private val repository: DeepLinkRepository,
) : LaunchDeepLink {

    private val application = UIApplication.sharedApplication

    override suspend fun launch(url: String): LaunchDeepLink.Result {
        val nsurl = NSURL.URLWithString(url)

        return if (nsurl != null && application.canOpenURL(nsurl)) {
            application.openURL(
                url = nsurl,
                options = emptyMap<Any?, Any>(),
                completionHandler = {},
            )

            LaunchDeepLink.Result.Success(url)
        } else {
            LaunchDeepLink.Result.Failure(IllegalArgumentException("Cannot open URL"))
        }
    }

    override suspend fun launch(deepLink: DeepLink): LaunchDeepLink.Result =
        launch(deepLink.link).also { repository.recordLaunch(deepLink) }
}
