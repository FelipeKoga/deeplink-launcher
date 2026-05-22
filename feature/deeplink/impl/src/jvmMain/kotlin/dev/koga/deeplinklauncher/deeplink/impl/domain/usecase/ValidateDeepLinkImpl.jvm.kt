package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ValidateDeepLink
import java.net.URI

internal class ValidateDeepLinkImpl : ValidateDeepLink {
    override fun isValid(link: String): Boolean {
        return try {
            val uri = URI(link)
            uri.scheme != null
        } catch (e: Exception) {
            false
        }
    }
}
