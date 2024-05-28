package dev.koga.deeplinklauncher.usecase.deeplink

import java.net.URI
import java.net.URISyntaxException

actual class ValidateDeepLink {
    actual operator fun invoke(deepLink: String): Boolean {
        return try {
            isUriValid(deepLink)
        } catch (e: Throwable) {
            false
        }
    }

    private fun isUriValid(link: String): Boolean {
        return try {
            val uri = URI(link)
            uri.scheme != null && uri.host != null
        } catch (e: URISyntaxException) {
            false
        }
    }
}