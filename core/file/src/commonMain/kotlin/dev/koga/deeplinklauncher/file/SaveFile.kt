package dev.koga.deeplinklauncher.file

import dev.koga.deeplinklauncher.file.model.FileType

expect class SaveFile {

    operator fun invoke(
        fileName: String,
        fileContent: String,
        type: FileType,
    ): String?

}