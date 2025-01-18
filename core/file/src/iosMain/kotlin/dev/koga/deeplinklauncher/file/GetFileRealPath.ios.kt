package dev.koga.deeplinklauncher.file

import platform.Foundation.NSURL

actual class GetFileRealPath {
    actual fun get(path: String): String {
        val fileURL = NSURL.fileURLWithPath(path)
        return fileURL.path ?: ""
    }
}
