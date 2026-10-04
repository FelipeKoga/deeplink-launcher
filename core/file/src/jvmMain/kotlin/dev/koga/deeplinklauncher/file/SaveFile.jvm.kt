package dev.koga.deeplinklauncher.file

import dev.koga.deeplinklauncher.file.model.FileType
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@SingleIn(AppScope::class)
@Inject
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
