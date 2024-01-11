package dev.koga.deeplinklauncher.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.model.Folder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map

class FolderRepository(
    private val database: DeepLinkLauncherDatabase
) {
    fun getFolders() = database
        .deepLinkLauncherDatabaseQueries
        .selectAllFolders()
        .asFlow()
        .mapToList(Dispatchers.IO)
        .map { data ->
            data.map {
                Folder(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    color = it.color
                )
            }

        }

    fun upsertFolder(folder: Folder) {
        database.deepLinkLauncherDatabaseQueries.upsertFolder(
            id = folder.id,
            name = folder.name,
            description = folder.description,
            color = folder.color
        )
    }
}