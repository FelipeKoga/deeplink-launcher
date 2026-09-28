package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import android.content.Context
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.domain.deeplink.impl.platform.android.createDeepLinkViewIntent
internal class LaunchDeepLinkImpl(
    private val context: Context,
    private val repository: DeepLinkRepository,
) : LaunchDeepLink {

    override suspend fun launch(url: String): LaunchDeepLink.Result {
        return launch(url, targetPackage = null)
    }

    override suspend fun launch(deepLink: DeepLink): LaunchDeepLink.Result =
        launch(deepLink.link, deepLink.targetPackage).also { repository.recordLaunch(deepLink) }

    private suspend fun launch(
        url: String,
        targetPackage: String?,
    ): LaunchDeepLink.Result {
        return try {
            val intent = context.createDeepLinkViewIntent(url, targetPackage)
            context.startActivity(intent)
            LaunchDeepLink.Result.Success(url)
        } catch (e: Throwable) {
            LaunchDeepLink.Result.Failure(e)
        }
    }
}
