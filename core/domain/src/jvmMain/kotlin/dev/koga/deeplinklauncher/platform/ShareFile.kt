package dev.koga.deeplinklauncher.platform

import dev.koga.deeplinklauncher.model.FileType
import java.awt.Desktop
import java.io.File
import java.io.IOException

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
                } else {
                    println("Opening files is not supported on this system.")
                }
            } else {
                println("Desktop is not supported on this system.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
