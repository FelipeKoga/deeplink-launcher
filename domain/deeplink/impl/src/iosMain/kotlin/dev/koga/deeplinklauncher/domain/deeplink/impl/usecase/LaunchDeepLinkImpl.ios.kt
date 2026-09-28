package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.LaunchDeepLink
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

internal class LaunchDeepLinkImpl(
    private val repository: DeepLinkRepository,
) : LaunchDeepLink {

    private val application = UIApplication.sharedApplication

    override suspend fun launch(url: String): LaunchDeepLink.Result {
        val nsurl = NSURL(string = url)

        return if (application.canOpenURL(nsurl)) {
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
