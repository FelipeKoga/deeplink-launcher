package dev.koga.deeplinklauncher.file

import androidx.compose.runtime.Composable
import dev.koga.deeplinklauncher.model.FileType

expect class BrowseFileAndGetPath {
    fun launch()

    @Composable
    fun Listen(onResult: (realPath: String, fileType: FileType?) -> Unit)
}
