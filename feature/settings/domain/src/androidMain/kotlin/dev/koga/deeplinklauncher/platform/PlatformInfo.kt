package dev.koga.deeplinklauncher.platform

import android.content.Context

actual class PlatformInfo(
    private val context: Context,
) {
    actual val version: String
        get() = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    actual val storePath: String
        get() = "https://play.google.com/store/apps/details?id=${context.packageName}"
}
