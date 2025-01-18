package dev.koga.deeplinklauncher.deeplink.impl.usecase

import android.net.Uri
import dev.koga.deeplinklauncher.deeplink.api.usecase.ValidateDeepLink

actual class ValidateDeepLinkImpl : ValidateDeepLink {
    actual override fun isValid(link: String): Boolean {
        return try {
            val uri = Uri.parse(link)
            uri.scheme != null && uri.host != null
        } catch (e: Exception) {
            false
        }
    }
}
