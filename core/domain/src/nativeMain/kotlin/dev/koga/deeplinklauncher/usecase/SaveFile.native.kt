package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.FileType

actual class SaveFile {
    actual operator fun invoke(
        fileName: String,
        fileContent: String,
        type: FileType,
    ): String? {
        TODO("Not yet implemented")
    }
}
