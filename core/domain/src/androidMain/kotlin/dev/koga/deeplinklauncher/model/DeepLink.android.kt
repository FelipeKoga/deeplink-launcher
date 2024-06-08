package dev.koga.deeplinklauncher.model

import android.net.Uri

actual val String.isLinkValid: Boolean
    get() {
        return try {
            val uri = Uri.parse(this)
            uri.scheme != null && uri.host != null
        } catch (e: Exception) {
            false
        }
    }
