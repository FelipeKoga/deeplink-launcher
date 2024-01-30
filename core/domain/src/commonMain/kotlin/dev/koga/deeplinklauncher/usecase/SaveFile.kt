package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.FileType

typealias FilePath = String

expect class SaveFile {
    operator fun invoke(
        fileName: String,
        fileContent: String,
        type: FileType,
    ): FilePath?
}
