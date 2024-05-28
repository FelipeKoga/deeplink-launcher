package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.FileType

expect class ShareFile {
    operator fun invoke(filePath: String, fileType: FileType)
}
