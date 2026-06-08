package dev.koga.deeplinklauncher.deeplink.impl.platform.android

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

internal fun Context.createDeepLinkViewIntent(
    link: String,
    targetPackage: String? = null,
): Intent = Intent(Intent.ACTION_VIEW, link.trim().toUri()).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    targetPackage?.let(::setPackage)
}

internal fun createDeepLinkViewIntent(
    link: String,
    targetPackage: String? = null,
): Intent = Intent(Intent.ACTION_VIEW, link.trim().toUri()).apply {
    targetPackage?.let(::setPackage)
}

internal fun handlerCacheKey(link: String, targetPackage: String?): String =
    "${link.trim()}|${targetPackage.orEmpty()}"
