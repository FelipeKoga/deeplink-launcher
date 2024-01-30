package dev.koga.deeplinklauncher.usecase.deeplink

import android.content.Context
import android.content.Intent
import android.net.Uri

actual class LaunchDeepLink(
    private val context: Context,
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
}
