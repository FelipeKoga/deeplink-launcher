package dev.koga.deeplinklauncher.usecase.folder

import dev.koga.deeplinklauncher.datasource.FolderDataSource

class GetFolderDeepLinksStream(
    private val dataSource: FolderDataSource,
) {
    operator fun invoke(folderId: String) = dataSource.getFolderDeepLinksStream(folderId)
}
