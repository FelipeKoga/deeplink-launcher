package dev.koga.deeplinklauncher.file

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

@SingleIn(AppScope::class)
@Inject
actual class GetFileContent {
    @OptIn(ExperimentalForeignApi::class)
    actual operator fun invoke(path: String): String {
        val content =
            NSString.stringWithContentsOfFile(path, encoding = NSUTF8StringEncoding, error = null)
        return content ?: return ""
    }
}
