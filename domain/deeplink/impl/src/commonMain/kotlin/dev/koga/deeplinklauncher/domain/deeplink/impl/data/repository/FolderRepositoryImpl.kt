package dev.koga.deeplinklauncher.domain.deeplink.impl.data.repository
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.GetFolderDeepLinks
import dev.koga.deeplinklauncher.database.SelectFoldersWithDeeplinkCount
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.FolderRepository.InsertResult
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.FolderRepository.WriteResult
import dev.koga.deeplinklauncher.domain.deeplink.impl.data.mapper.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class FolderRepositoryImpl(
    private val database: DeepLinkLauncherDatabase,
) : FolderRepository {

    private val queries get() = database.folderQueries

    override fun getFoldersStream(): Flow<List<Folder>> {
        return queries
            .selectFoldersWithDeeplinkCount()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { data -> data.map(SelectFoldersWithDeeplinkCount::toDomain) }
    }

    override fun getFolders(): List<Folder> {
        return queries
            .selectFoldersWithDeeplinkCount()
            .executeAsList()
            .map(SelectFoldersWithDeeplinkCount::toDomain)
    }

    override fun getFolderDeepLinksStream(id: String): Flow<List<DeepLink>> {
        return queries
            .getFolderDeepLinks(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map(GetFolderDeepLinks::toDomain) }
    }

    override fun getFolderByIdStream(id: String): Flow<Folder?> {
        return queries
            .getFolderById(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows -> rows.singleOrNull()?.toDomain() }
    }

    override fun getFolderById(id: String): Folder? {
        return queries
            .getFolderById(id)
            .executeAsOneOrNull()
            ?.toDomain()
    }

    override suspend fun insert(folder: Folder): InsertResult = write {
        database.transactionWithResult {
            if (isNameUsedByOther(name = folder.name, id = folder.id)) {
                return@transactionWithResult InsertResult.NameAlreadyExists
            }

            queries.insertFolder(id = folder.id, name = folder.name, description = folder.description)
            InsertResult.Success
        }
    }

    override suspend fun update(id: String, name: String, description: String?): WriteResult = write {
        database.transactionWithResult {
            when {
                queries.countFoldersById(id).executeAsOne() == 0L -> WriteResult.NotFound
                isNameUsedByOther(name = name, id = id) -> WriteResult.NameAlreadyExists
                else -> {
                    queries.updateFolder(name = name, description = description, id = id)
                    WriteResult.Success
                }
            }
        }
    }

    override suspend fun delete(id: String) {
        write {
            database.transaction {
                queries.removeFolderFromDeeplinks(id)
                queries.deleteFolderById(id)
            }
        }
    }

    override suspend fun deleteAll() {
        write {
            database.transaction {
                queries.removeAllFoldersFromDeeplinks()
                queries.deleteAllFolders()
            }
        }
    }

    private fun isNameUsedByOther(name: String, id: String): Boolean =
        queries.countOtherFoldersWithName(name = name, id = id).executeAsOne() > 0

    private suspend fun <T> write(block: () -> T): T = withContext(Dispatchers.IO) { block() }
}
