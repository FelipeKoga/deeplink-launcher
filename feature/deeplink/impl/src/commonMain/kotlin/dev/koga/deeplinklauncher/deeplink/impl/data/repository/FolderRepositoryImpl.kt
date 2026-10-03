package dev.koga.deeplinklauncher.deeplink.impl.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.GetFolderDeepLinks
import dev.koga.deeplinklauncher.database.SelectFoldersWithDeeplinkCount
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.impl.data.mapper.toDomain
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
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

    override fun getFolderByIdStream(id: String): Flow<Folder?> {
        return database.folderQueries
            .getFolderById(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows -> rows.singleOrNull()?.toDomain() }
    }

    override fun getFolderById(id: String): Folder? {
        return database.folderQueries
            .getFolderById(id)
            .executeAsOneOrNull()
            ?.toDomain()
    }

    override fun upsertFolder(folder: Folder): FolderRepository.UpsertResult =
        database.transactionWithResult {
            val queries = database.folderQueries
            val updatedRows = queries.updateFolder(
                name = folder.name,
                description = folder.description,
                id = folder.id,
            ).value
            if (updatedRows > 0L) return@transactionWithResult FolderRepository.UpsertResult.Saved

            val existingId = queries.selectOtherFolderIdByName(name = folder.name, id = folder.id)
                .executeAsOneOrNull()
            if (existingId != null) {
                return@transactionWithResult FolderRepository.UpsertResult.NameAlreadyExists(existingId)
            }

            queries.insertFolder(
                id = folder.id,
                name = folder.name,
                description = folder.description,
            )
            FolderRepository.UpsertResult.Saved
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
