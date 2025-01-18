package dev.koga.deeplinklauncher.file

import dev.koga.deeplinklauncher.file.model.FileType

expect class ShareFile {
    operator fun invoke(filePath: String, fileType: FileType)
}
