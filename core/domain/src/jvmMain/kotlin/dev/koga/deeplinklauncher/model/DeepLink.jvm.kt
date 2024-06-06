package dev.koga.deeplinklauncher.model

import java.net.URI

actual val String.isLinkValid: Boolean
    get() {
        return try {
            val uri = URI(this)
            uri.scheme != null && uri.host != null
        } catch (e: Exception) {
            false
        }
    }