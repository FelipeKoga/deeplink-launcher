package dev.koga.deeplinklauncher.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.datetime.Instant

class FolderRepository(
    private val database: DeepLinkLauncherDatabase
) {
    fun getFolders() = database
        .deepLinkLauncherDatabaseQueries
        .selectFoldersWithDeeplinkCount()
        .asFlow()
        .mapToList(Dispatchers.IO)
        .map { data ->
            data.map {
                Folder(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    deepLinkCount = it.deeplinkCount.toInt()
                )
            }

        }

    fun upsertFolder(folder: Folder) {
        database.deepLinkLauncherDatabaseQueries.upsertFolder(
            id = folder.id,
            name = folder.name,
            description = folder.description,
        )
    }

    fun deleteFolderById(folderId: String) {
        database.deepLinkLauncherDatabaseQueries.deleteFolderById(folderId)
    }

    fun getFolderById(folderId: String): Flow<Folder?> {
        return database.deepLinkLauncherDatabaseQueries
            .getFolderById(folderId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .mapNotNull {
                it.firstOrNull()?.let { data ->
                    Folder(
                        id = data.id,
                        name = data.name,
                        description = data.description,
                        deepLinkCount = data.deeplinkCount.toInt()
                    )
                }
            }
    }

    fun getFolderDeepLinks(folderId: String): Flow<List<DeepLink>> {
        return database.deepLinkLauncherDatabaseQueries.getFolderDeepLinks(folderId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map {
                it.map { data ->
                    DeepLink(
                        id = data.id,
                        link = data.link,
                        name = data.name,
                        description = data.description,
                        createdAt = Instant.fromEpochMilliseconds(data.createdAt),
                        isFavorite = data.isFavorite == 1L,
                    )
                }
            }
    }
}