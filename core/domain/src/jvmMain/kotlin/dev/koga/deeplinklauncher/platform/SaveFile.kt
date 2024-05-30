package dev.koga.deeplinklauncher.platform

import dev.koga.deeplinklauncher.model.FileType
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

actual class SaveFile {
    actual operator fun invoke(
        fileName: String,
        fileContent: String,
        type: FileType,
    ): String? {
        return try {
            val userHome = System.getProperty("user.home")
            val downloadsPath = File(userHome, "Downloads")
            if (!downloadsPath.exists()) {
                downloadsPath.mkdirs()
            }

            val file = File(downloadsPath, fileName)
            FileOutputStream(file).use { outputStream ->
                outputStream.write(fileContent.toByteArray())
            }

            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
