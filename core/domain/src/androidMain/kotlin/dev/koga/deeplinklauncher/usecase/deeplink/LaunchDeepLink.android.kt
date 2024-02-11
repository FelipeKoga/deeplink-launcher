package dev.koga.deeplinklauncher.usecase.deeplink

import android.content.Context
import android.content.Intent
import android.net.Uri
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.util.currentLocalDateTime

actual class LaunchDeepLink(
    private val context: Context,
    private val dataSource: DeepLinkDataSource,
) {
    actual fun launch(url: String): LaunchDeepLinkResult {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            LaunchDeepLinkResult.Success(url)
        } catch (e: Throwable) {
            LaunchDeepLinkResult.Failure(e)
        }
    }

    actual fun launch(deepLink: DeepLink): LaunchDeepLinkResult {
        dataSource.upsertDeepLink(deepLink.copy(lastLaunchedAt = currentLocalDateTime))

        return launch(deepLink.link)
    }
}
