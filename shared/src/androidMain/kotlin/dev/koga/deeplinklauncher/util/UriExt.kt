package dev.koga.deeplinklauncher.util

import java.net.URI


fun String.isUriValid(): Boolean {
    return try {
        val uri = URI(this)
        uri.scheme != null && uri.host != null
    } catch (e: Throwable) {
        false
    }
}