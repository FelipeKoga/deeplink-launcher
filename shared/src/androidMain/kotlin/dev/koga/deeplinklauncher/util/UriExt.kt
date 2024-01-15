package dev.koga.deeplinklauncher.util

import android.net.Uri


fun String.toUriOrNull(): Uri? {
    return try {
        Uri.parse(this)
    } catch (e: Exception) {
        null
    }
}