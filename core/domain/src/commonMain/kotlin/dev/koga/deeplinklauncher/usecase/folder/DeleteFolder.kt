package dev.koga.deeplinklauncher.usecase.folder

import dev.koga.deeplinklauncher.datasource.FolderDataSource

class DeleteFolder(
    private val dataSource: FolderDataSource
) {
    operator fun invoke(id: String) = dataSource.deleteFolder(id)
}