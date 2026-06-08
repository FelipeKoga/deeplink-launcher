package dev.koga.deeplinklauncher.platform

import java.io.File

fun migrateFileIfNeeded(
    legacyFile: File,
    targetFile: File,
    rename: (File, File) -> Boolean = File::renameTo,
) {
    if (targetFile.exists() || !legacyFile.exists()) return

    targetFile.parentFile?.mkdirs()

    val migrated = rename(legacyFile, targetFile) ||
        run {
            legacyFile.copyTo(targetFile, overwrite = false)
            true
        }

    if (migrated && targetFile.exists()) {
        legacyFile.delete()
    }
}
