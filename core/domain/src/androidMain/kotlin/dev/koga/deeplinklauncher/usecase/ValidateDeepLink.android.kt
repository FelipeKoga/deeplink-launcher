package dev.koga.deeplinklauncher.usecase

import java.net.URI

actual class ValidateDeepLink {
    actual operator fun invoke(deepLink: String): Boolean {
        return try {
            val uri = URI(deepLink)
            uri.scheme != null && uri.host != null
        } catch (e: Throwable) {
            false
        }
    }
}
