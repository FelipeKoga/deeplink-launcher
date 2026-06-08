package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import android.content.Context
import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.impl.platform.android.createDeepLinkViewIntent

internal class LaunchDeepLinkImpl(
    private val context: Context,
    private val repository: DeepLinkRepository,
) : LaunchDeepLink {

    override suspend fun launch(url: String): LaunchDeepLink.Result {
        return launch(url, targetPackage = null)
    }

    override suspend fun launch(deepLink: DeepLink): LaunchDeepLink.Result {
        repository.upsertDeepLink(deepLink.copy(lastLaunchedAt = currentLocalDateTime))

        return launch(deepLink.link, deepLink.targetPackage)
    }

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
