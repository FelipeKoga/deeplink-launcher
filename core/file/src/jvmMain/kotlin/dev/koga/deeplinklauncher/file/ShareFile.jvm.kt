package dev.koga.deeplinklauncher.file

import dev.koga.deeplinklauncher.file.model.FileType
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.awt.Desktop
import java.io.File
import java.io.IOException

@SingleIn(AppScope::class)
@Inject
actual class ShareFile {
    actual operator fun invoke(
        filePath: String,
        fileType: FileType,
    ) {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                throw IOException("File does not exist: $filePath")
            }

            if (Desktop.isDesktopSupported()) {
                val desktop = Desktop.getDesktop()
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(file)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
