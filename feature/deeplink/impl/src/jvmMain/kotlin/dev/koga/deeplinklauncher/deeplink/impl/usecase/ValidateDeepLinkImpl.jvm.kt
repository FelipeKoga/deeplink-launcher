package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.usecase.ValidateDeepLink
import java.net.URI

actual class ValidateDeepLinkImpl : ValidateDeepLink {
    actual override fun isValid(link: String): Boolean {
        return try {
            val uri = URI(link)
            uri.scheme != null && uri.host != null
        } catch (e: Exception) {
            false
        }
    }
}
