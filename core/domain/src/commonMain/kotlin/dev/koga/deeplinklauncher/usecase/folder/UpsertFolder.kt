package dev.koga.deeplinklauncher.usecase.folder

import dev.koga.deeplinklauncher.datasource.FolderDataSource
import dev.koga.deeplinklauncher.model.Folder

class UpsertFolder(
    private val dataSource: FolderDataSource
) {
    operator fun invoke(folder: Folder) = dataSource.upsertFolder(folder)
}