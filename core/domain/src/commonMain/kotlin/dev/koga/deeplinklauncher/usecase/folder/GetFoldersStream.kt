package dev.koga.deeplinklauncher.usecase.folder

import dev.koga.deeplinklauncher.datasource.FolderDataSource

class GetFoldersStream(
    private val dataSource: FolderDataSource,
) {
    operator fun invoke() = dataSource.getFoldersStream()
}
