package dev.koga.deeplinklauncher.file

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import platform.Foundation.NSURL

@SingleIn(AppScope::class)
@Inject
actual class GetFileRealPath {
    actual fun get(path: String): String {
        val fileURL = NSURL.fileURLWithPath(path)
        return fileURL.path ?: ""
    }
}
