package dev.koga.deeplinklauncher.platform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSString
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.NSUTF8StringEncoding

actual class GetFileContent {
    @OptIn(ExperimentalForeignApi::class)
    actual operator fun invoke(path: String): String {
        val content =
            NSString.stringWithContentsOfFile(path, encoding = NSUTF8StringEncoding, error = null)
        return content ?: return ""
    }
}
