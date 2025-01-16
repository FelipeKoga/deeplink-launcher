package dev.koga.deeplinklauncher.deeplink.impl.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink

internal class LaunchAndroidDeepLink(
    private val context: Context,
    private val repository: DeepLinkRepository,
) : LaunchDeepLink {

    override suspend fun launch(url: String): LaunchDeepLink.Result {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            LaunchDeepLink.Result.Success(url)
        } catch (e: Throwable) {
            LaunchDeepLink.Result.Failure(e)
        }
    }

    override suspend fun launch(deepLink: DeepLink): LaunchDeepLink.Result {
        repository.upsertDeepLink(deepLink.copy(lastLaunchedAt = currentLocalDateTime))

        return launch(deepLink.link)
    }

}