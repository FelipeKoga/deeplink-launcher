package dev.koga.deeplinklauncher.usecase.folder

import dev.koga.deeplinklauncher.datasource.FolderDataSource

class GetFolderById(
    private val dataSource: FolderDataSource,
) {
    operator fun invoke(folderId: String) = dataSource.getFolderById(folderId)
}