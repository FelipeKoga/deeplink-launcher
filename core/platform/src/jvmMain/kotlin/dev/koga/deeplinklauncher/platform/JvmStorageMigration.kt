package dev.koga.deeplinklauncher.platform

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files

fun migrateFileIfNeeded(
    legacyFile: File,
    targetFile: File,
    rename: (File, File) -> Boolean = File::renameTo,
    copy: (File, File) -> Unit = ::copyAndSync,
) {
    if (targetFile.exists() || !legacyFile.exists()) return

    targetFile.parentFile?.mkdirs()

    if (rename(legacyFile, targetFile)) return

    val partialFile = File("${targetFile.path}.migrating")
    try {
        copy(legacyFile, partialFile)
        Files.move(partialFile.toPath(), targetFile.toPath())
    } finally {
        partialFile.delete()
    }

    legacyFile.delete()
}

private fun copyAndSync(source: File, target: File) {
    FileInputStream(source).use { input ->
        FileOutputStream(target).use { output ->
            input.copyTo(output)
            output.fd.sync()
        }
    }
}
