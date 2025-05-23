package dev.koga.deeplinklauncher.file

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

actual class GetFileContent {
    @OptIn(ExperimentalForeignApi::class)
    actual operator fun invoke(path: String): String {
        val content =
            NSString.stringWithContentsOfFile(path, encoding = NSUTF8StringEncoding, error = null)
        return content ?: return ""
    }
}
