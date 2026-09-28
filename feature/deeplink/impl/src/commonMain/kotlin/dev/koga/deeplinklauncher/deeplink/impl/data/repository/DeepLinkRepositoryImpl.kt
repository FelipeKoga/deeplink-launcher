@file:OptIn(ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.deeplink.impl.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.GetDeepLinkByLink
import dev.koga.deeplinklauncher.database.SelectAllDeeplinks
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository.WriteResult
import dev.koga.deeplinklauncher.deeplink.impl.data.mapper.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class DeepLinkRepositoryImpl(
    private val database: DeepLinkLauncherDatabase,
) : DeepLinkRepository {

    private val queries get() = database.deepLinkQueries
    private val folderQueries get() = database.folderQueries

    override fun getDeepLinksStream(): Flow<List<DeepLink>> {
        return queries
            .selectAllDeeplinks()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map(SelectAllDeeplinks::toDomain) }
    }

    override fun getDeepLinks(): List<DeepLink> {
        return queries
            .selectAllDeeplinks()
            .executeAsList()
            .map(SelectAllDeeplinks::toDomain)
    }

    override fun getDeepLinkByIdStream(id: String): Flow<DeepLink?> {
        return queries
            .getDeepLinkById(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.singleOrNull()?.toDomain() }
    }

    override fun getDeepLinkById(id: String): DeepLink? {
        return queries
            .getDeepLinkById(id)
            .executeAsOneOrNull()
            ?.toDomain()
    }

    override fun getDeepLinkByLink(link: String): DeepLink? {
        return queries
            .getDeepLinkByLink(link)
            .executeAsOneOrNull()
            ?.let(GetDeepLinkByLink::toDomain)
    }

    override suspend fun insert(deepLink: DeepLink): WriteResult = write {
        database.transactionWithResult {
            if (isLinkUsedByOther(link = deepLink.link, id = deepLink.id)) {
                return@transactionWithResult WriteResult.LinkAlreadyExists
            }

            insertRow(deepLink, id = deepLink.id, folderId = deepLink.folder?.id)
            WriteResult.Success
        }
    }

    override suspend fun updateLink(id: String, link: String): WriteResult = write {
        database.transactionWithResult {
            when {
                !exists(id) -> WriteResult.NotFound
                isLinkUsedByOther(link = link, id = id) -> WriteResult.LinkAlreadyExists
                else -> {
                    queries.updateLink(link = link, id = id)
                    WriteResult.Success
                }
            }
        }
    }

    override suspend fun updateName(id: String, name: String?) {
        write {
            queries.updateName(name = name, id = id)
        }
    }

    override suspend fun updateDescription(id: String, description: String?) {
        write {
            queries.updateDescription(description = description, id = id)
        }
    }

    override suspend fun updateTargetPackage(id: String, targetPackage: String?) {
        write {
            queries.updateTargetPackage(targetPackage = targetPackage, id = id)
        }
    }

    override suspend fun setFavorite(id: String, isFavorite: Boolean) {
        write {
            queries.updateFavorite(isFavorite = isFavorite.toLong(), id = id)
        }
    }

    override suspend fun setFolder(id: String, folderId: String?) {
        write {
            queries.updateFolder(folderId = folderId, id = id)
        }
    }

    override suspend fun recordLaunch(id: String, launchedAt: LocalDateTime) {
        write {
            queries.updateLastLaunchedAt(lastLaunchedAt = launchedAt, id = id)
        }
    }

    override suspend fun delete(id: String) {
        write {
            queries.deleteDeeplinkById(id)
        }
    }

    override suspend fun deleteAll(): List<String> = write {
        database.transactionWithResult {
            queries.selectAllDeeplinkIds().executeAsList()
                .also { queries.deleteAllDeeplinks() }
        }
    }

    override suspend fun importAll(folders: List<Folder>, deepLinks: List<DeepLink>) {
        write {
            database.transaction {
                val folderIds = importFolders(folders)

                deepLinks.forEach { deepLink ->
                    val folderId = deepLink.folder?.id?.let { importedId ->
                        folderIds[importedId] ?: importedId.takeIf(::folderExists)
                    }
                    val existing = queries.getDeepLinkByLink(deepLink.link).executeAsOneOrNull()

                    if (existing != null) {
                        queries.updateImportedFields(
                            name = deepLink.name,
                            description = deepLink.description,
                            createdAt = deepLink.createdAt,
                            isFavorite = deepLink.isFavorite.toLong(),
                            folderId = folderId,
                            targetPackage = deepLink.targetPackage,
                            id = existing.id,
                        )
                    } else {
                        val id = deepLink.id.takeUnless(::exists) ?: Uuid.random().toString()
                        insertRow(deepLink, id = id, folderId = folderId)
                    }
                }
            }
        }
    }

    /** Returns, for each imported folder id, the local folder id that now holds it. */
    private fun importFolders(folders: List<Folder>): Map<String, String> =
        folders.associate { folder ->
            val sameNameId = folderQueries.getFolderIdByName(folder.name).executeAsOneOrNull()
            when {
                sameNameId != null -> {
                    if (sameNameId == folder.id) {
                        folderQueries.updateFolder(
                            name = folder.name,
                            description = folder.description,
                            id = folder.id,
                        )
                    }
                    folder.id to sameNameId
                }

                folderExists(folder.id) -> {
                    folderQueries.updateFolder(
                        name = folder.name,
                        description = folder.description,
                        id = folder.id,
                    )
                    folder.id to folder.id
                }

                else -> {
                    folderQueries.insertFolder(
                        id = folder.id,
                        name = folder.name,
                        description = folder.description,
                    )
                    folder.id to folder.id
                }
            }
        }

    private fun insertRow(deepLink: DeepLink, id: String, folderId: String?) {
        queries.insertDeeplink(
            id = id,
            link = deepLink.link,
            name = deepLink.name,
            description = deepLink.description,
            createdAt = deepLink.createdAt,
            lastLaunchedAt = deepLink.lastLaunchedAt,
            isFavorite = deepLink.isFavorite.toLong(),
            folderId = folderId,
            targetPackage = deepLink.targetPackage,
        )
    }

    private fun exists(id: String): Boolean =
        queries.countDeeplinksById(id).executeAsOne() > 0

    private fun isLinkUsedByOther(link: String, id: String): Boolean =
        queries.countOtherDeeplinksWithLink(link = link, id = id).executeAsOne() > 0

    private fun folderExists(id: String): Boolean =
        folderQueries.countFoldersById(id).executeAsOne() > 0

    private suspend fun <T> write(block: () -> T): T = withContext(Dispatchers.IO) { block() }

    private fun Boolean.toLong(): Long = if (this) 1L else 0L
}
