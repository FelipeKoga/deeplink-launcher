package dev.koga.deeplinklauncher

import android.content.Context
import android.content.Intent
import android.net.Uri

actual class LaunchDeepLink(
    private val context: Context
) {
    actual fun launch(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}