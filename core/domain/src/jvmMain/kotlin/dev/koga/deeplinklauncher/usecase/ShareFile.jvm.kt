package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.FileType

actual class ShareFile {
    actual operator fun invoke(
        filePath: String,
        fileType: FileType,
    ) {
    }
}
