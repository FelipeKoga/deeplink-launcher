package dev.koga.deeplinklauncher.deeplink.common.usecase

import android.net.Uri

internal class ValidateDeepLinkImpl : ValidateDeepLink {
    override fun isValid(link: String): Boolean {
        return try {
            val uri = Uri.parse(link)
            uri.scheme != null
        } catch (e: Exception) {
            false
        }
    }
}