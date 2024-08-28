package dev.koga.deeplinklauncher.platform

import dev.koga.deeplinklauncher.model.FileType
import kotlinx.cinterop.ExperimentalForeignApi

import platform.Foundation.*

actual class SaveFile {
    @OptIn(ExperimentalForeignApi::class)
    actual operator fun invoke(
        fileName: String,
        fileContent: String,
        type: FileType,
    ): String? {
        val fileManager = NSFileManager.defaultManager
        val directories = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
        val documentDirectory = directories.first() as NSURL
        val fileUrl = documentDirectory.URLByAppendingPathComponent(fileName) ?: return ""

        val success = fileContent.nsString.writeToURL(fileUrl, atomically = true, encoding = NSUTF8StringEncoding, error = null)

        return if (success) {
            fileUrl.absoluteString
        } else {
            null
        }
    }

    private val String.nsString: NSString
        get() = NSString.create(string = this)
}