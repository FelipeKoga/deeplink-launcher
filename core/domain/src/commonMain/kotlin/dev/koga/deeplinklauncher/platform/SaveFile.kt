package dev.koga.deeplinklauncher.platform

import dev.koga.deeplinklauncher.model.FileType

expect class SaveFile {
    operator fun invoke(
        fileName: String,
        fileContent: String,
        type: FileType,
    ): String?
}
