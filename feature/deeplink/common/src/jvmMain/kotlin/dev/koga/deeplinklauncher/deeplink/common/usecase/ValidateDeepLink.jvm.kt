package dev.koga.deeplinklauncher.deeplink.common.usecase

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