package dev.koga.deeplinklauncher.usecase

import java.net.URI

actual class ValidateDeepLink {
    actual operator fun invoke(deepLink: String): Boolean {
        return try {
            return isUriValid(deepLink)
        } catch (e: Throwable) {
            false
        }
    }

    private fun isUriValid(link: String): Boolean {
        val uri = URI(link)
        return uri.scheme != null && uri.host != null
    }
}
