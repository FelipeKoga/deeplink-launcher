package dev.koga.deeplinklauncher.deeplink.common.repository.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.GetFolderById
import dev.koga.deeplinklauncher.database.GetFolderDeepLinks
import dev.koga.deeplinklauncher.database.SelectFoldersWithDeeplinkCount
import dev.koga.deeplinklauncher.deeplink.common.mapper.toDomain
import dev.koga.deeplinklauncher.deeplink.common.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.common.model.Folder
import dev.koga.deeplinklauncher.deeplink.common.repository.FolderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class FolderRepositoryImpl(
    private val database: DeepLinkLauncherDatabase,
) : FolderRepository {

    override fun getFoldersStream(): Flow<List<Folder>> {
        return database
            .folderQueries
            .selectFoldersWithDeeplinkCount()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { data -> data.map(SelectFoldersWithDeeplinkCount::toDomain) }
    }

    override fun getFolders(): List<Folder> {
        return database
            .folderQueries
            .selectFoldersWithDeeplinkCount()
            .executeAsList()
            .map(SelectFoldersWithDeeplinkCount::toDomain)
    }

    override fun getFolderDeepLinksStream(id: String): Flow<List<DeepLink>> {
        return database
            .folderQueries
            .getFolderDeepLinks(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map(GetFolderDeepLinks::toDomain) }
    }

    override fun getFolderById(id: String): Folder {
        return database.folderQueries
            .getFolderById(id)
            .executeAsOne()
            .let(GetFolderById::toDomain)
    }

    override fun upsertFolder(folder: Folder) {
        database.folderQueries.upsertFolder(
            id = folder.id,
            name = folder.name,
            description = folder.description,
        )
    }

    override fun deleteFolder(id: String) {
        database.transaction {
            database.folderQueries.removeFolderFromDeeplinks(id)
            database.folderQueries.deleteFolderById(id)
        }
    }

    override fun deleteAll() {
        database.transaction {
            val folders = database.folderQueries.selectAllFoldersIds().executeAsList()
            folders.forEach { folderId ->
                database.folderQueries.removeFolderFromDeeplinks(folderId)
            }

            database.folderQueries.deleteAllFolders()
        }
    }
}
