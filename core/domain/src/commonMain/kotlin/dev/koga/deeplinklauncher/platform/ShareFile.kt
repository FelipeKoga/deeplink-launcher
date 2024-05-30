package dev.koga.deeplinklauncher.platform

import dev.koga.deeplinklauncher.model.FileType

expect class ShareFile {
    operator fun invoke(filePath: String, fileType: FileType)
}
