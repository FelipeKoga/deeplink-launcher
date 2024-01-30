package dev.koga.deeplinklauncher

import androidx.compose.runtime.Composable
import dev.koga.deeplinklauncher.model.FileType

actual class BrowseFileAndGetPath {
    actual fun launch() {
    }

    @Composable
    actual fun Listen(onResult: (realPath: String, fileType: FileType?) -> Unit) {
    }
}
