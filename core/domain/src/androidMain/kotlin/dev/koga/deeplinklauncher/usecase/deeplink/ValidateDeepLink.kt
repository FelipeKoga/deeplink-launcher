package dev.koga.deeplinklauncher.usecase.deeplink

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import java.net.URI

actual class ValidateDeepLink(
    private val context: Context
) {
    actual operator fun invoke(deepLink: String): Boolean {
        return try {
            if (isUriValid(deepLink)) return false

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val activities = context
                .packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

            return activities.isNotEmpty()
        } catch (e: Throwable) {
            false
        }
    }

    private fun isUriValid(link: String): Boolean {
        val uri = URI(link)
        return uri.scheme != null && uri.host != null
    }
}