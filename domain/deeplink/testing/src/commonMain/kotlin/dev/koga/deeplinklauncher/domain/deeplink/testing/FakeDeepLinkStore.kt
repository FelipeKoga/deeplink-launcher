@file:OptIn(ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.domain.deeplink.testing

import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * In-memory deeplink storage with the same observable behavior as the SQL implementation:
 * link and folder-name uniqueness, deeplinks referencing folders by id, atomic imports.
 * [DeepLinkRepositoryContract] keeps both implementations in sync.
 */
public class FakeDeepLinkStore {
    private val rows = MutableStateFlow<Map<String, Row>>(emptyMap())
    private val folderRows = MutableStateFlow<Map<String, Folder>>(emptyMap())

    public val deepLinkRepository: DeepLinkRepository = FakeDeepLinkRepository()
    public val folderRepository: FolderRepository = FakeFolderRepository()

    private data class Row(val deepLink: DeepLink, val folderId: String?)

    private fun Row.resolve(folders: Map<String, Folder>): DeepLink = deepLink.copy(
        folder = folderId?.let { id ->
            val folder = folders[id]
            Folder(id = id, name = folder?.name.orEmpty(), description = folder?.description)
        },
    )

    private fun Map<String, Row>.resolved(folders: Map<String, Folder>): List<DeepLink> =
        values.map { it.resolve(folders) }.sortedWith(
            compareByDescending<DeepLink> { it.lastLaunchedAt }
                .thenByDescending { it.createdAt }
                .thenBy { it.name },
        )

    private fun Map<String, Folder>.withCounts(rows: Map<String, Row>): List<Folder> =
        values.map { folder -> folder.copy(deepLinkCount = rows.values.count { it.folderId == folder.id }) }

    private fun updateRow(id: String, transform: (Row) -> Row) {
        rows.update { current -> current[id]?.let { current + (id to transform(it)) } ?: current }
    }

    private inner class FakeDeepLinkRepository : DeepLinkRepository {
        override fun getDeepLinksStream(): Flow<List<DeepLink>> =
            combine(rows, folderRows) { rows, folders -> rows.resolved(folders) }

        override fun getDeepLinks(): List<DeepLink> = rows.value.resolved(folderRows.value)

        override fun getDeepLinkByIdStream(id: String): Flow<DeepLink?> =
            combine(rows, folderRows) { rows, folders -> rows[id]?.resolve(folders) }

        override fun getDeepLinkById(id: String): DeepLink? = rows.value[id]?.resolve(folderRows.value)

        override fun getDeepLinkByLink(link: String): DeepLink? =
            rows.value.values.firstOrNull { it.deepLink.link == link }?.resolve(folderRows.value)

        override suspend fun insert(deepLink: DeepLink): DeepLinkRepository.InsertResult {
            if (rows.value.values.any { it.deepLink.link == deepLink.link && it.deepLink.id != deepLink.id }) {
                return DeepLinkRepository.InsertResult.LinkAlreadyExists
            }
            check(deepLink.id !in rows.value) { "Duplicate id ${deepLink.id}" }
            rows.update { it + (deepLink.id to Row(deepLink.copy(folder = null), deepLink.folder?.id)) }
            return DeepLinkRepository.InsertResult.Success
        }

        override suspend fun updateLink(id: String, link: String): DeepLinkRepository.WriteResult = when {
            id !in rows.value -> DeepLinkRepository.WriteResult.NotFound
            rows.value.values.any { it.deepLink.link == link && it.deepLink.id != id } ->
                DeepLinkRepository.WriteResult.LinkAlreadyExists

            else -> {
                updateRow(id) { it.copy(deepLink = it.deepLink.copy(link = link)) }
                DeepLinkRepository.WriteResult.Success
            }
        }

        override suspend fun updateName(id: String, name: String?) =
            updateRow(id) { it.copy(deepLink = it.deepLink.copy(name = name)) }

        override suspend fun updateDescription(id: String, description: String?) =
            updateRow(id) { it.copy(deepLink = it.deepLink.copy(description = description)) }

        override suspend fun updateTargetPackage(id: String, targetPackage: String?) =
            updateRow(id) { it.copy(deepLink = it.deepLink.copy(targetPackage = targetPackage)) }

        override suspend fun setFavorite(id: String, isFavorite: Boolean) =
            updateRow(id) { it.copy(deepLink = it.deepLink.copy(isFavorite = isFavorite)) }

        override suspend fun setFolder(id: String, folderId: String?) =
            updateRow(id) { it.copy(folderId = folderId) }

        override suspend fun recordLaunch(id: String, launchedAt: LocalDateTime) =
            updateRow(id) { it.copy(deepLink = it.deepLink.copy(lastLaunchedAt = launchedAt)) }

        override suspend fun delete(id: String) {
            rows.update { it - id }
        }

        override suspend fun deleteAll(): List<String> {
            val ids = rows.value.keys.toList()
            rows.value = emptyMap()
            return ids
        }

        override suspend fun importAll(folders: List<Folder>, deepLinks: List<DeepLink>) {
            // Build the new state first and publish it at once: all or nothing.
            val newFolders = folderRows.value.toMutableMap()
            val folderIds = folders.associate { folder ->
                val sameName = newFolders.values.firstOrNull { it.name == folder.name }
                when {
                    sameName != null -> folder.id to sameName.id.also {
                        if (sameName.id == folder.id) newFolders[folder.id] = folder.copy(deepLinkCount = 0)
                    }

                    else -> {
                        newFolders[folder.id] = folder.copy(deepLinkCount = 0)
                        folder.id to folder.id
                    }
                }
            }

            val newRows = rows.value.toMutableMap()
            deepLinks.forEach { deepLink ->
                val folderId = deepLink.folder?.id?.let { folderIds[it] ?: it.takeIf(newFolders::containsKey) }
                val existing = newRows.values.firstOrNull { it.deepLink.link == deepLink.link }
                if (existing != null) {
                    newRows[existing.deepLink.id] = existing.copy(
                        deepLink = existing.deepLink.copy(
                            name = deepLink.name,
                            description = deepLink.description,
                            createdAt = deepLink.createdAt,
                            isFavorite = deepLink.isFavorite,
                            targetPackage = deepLink.targetPackage,
                        ),
                        folderId = folderId,
                    )
                } else {
                    val id = deepLink.id.takeUnless(newRows::containsKey) ?: Uuid.random().toString()
                    newRows[id] = Row(deepLink.copy(id = id, folder = null), folderId)
                }
            }

            folderRows.value = newFolders
            rows.value = newRows
        }
    }

    private inner class FakeFolderRepository : FolderRepository {
        override fun getFoldersStream(): Flow<List<Folder>> =
            combine(folderRows, rows) { folders, rows -> folders.withCounts(rows) }

        override fun getFolders(): List<Folder> = folderRows.value.withCounts(rows.value)

        override fun getFolderDeepLinksStream(id: String): Flow<List<DeepLink>> =
            combine(rows, folderRows) { rows, folders ->
                rows.filterValues { it.folderId == id }.resolved(folders)
            }

        override fun getFolderByIdStream(id: String): Flow<Folder?> =
            combine(folderRows, rows) { folders, rows -> folders.withCounts(rows).firstOrNull { it.id == id } }

        override fun getFolderById(id: String): Folder? = getFolders().firstOrNull { it.id == id }

        override suspend fun insert(folder: Folder): FolderRepository.InsertResult {
            if (folderRows.value.values.any { it.name == folder.name && it.id != folder.id }) {
                return FolderRepository.InsertResult.NameAlreadyExists
            }
            folderRows.update { it + (folder.id to folder.copy(deepLinkCount = 0)) }
            return FolderRepository.InsertResult.Success
        }

        override suspend fun update(id: String, name: String, description: String?): FolderRepository.WriteResult =
            when {
                id !in folderRows.value -> FolderRepository.WriteResult.NotFound
                folderRows.value.values.any { it.name == name && it.id != id } ->
                    FolderRepository.WriteResult.NameAlreadyExists

                else -> {
                    folderRows.update { it + (id to it.getValue(id).copy(name = name, description = description)) }
                    FolderRepository.WriteResult.Success
                }
            }

        override suspend fun delete(id: String) {
            rows.update { current -> current.mapValues { (_, row) -> if (row.folderId == id) row.copy(folderId = null) else row } }
            folderRows.update { it - id }
        }

        override suspend fun deleteAll() {
            rows.update { current -> current.mapValues { (_, row) -> row.copy(folderId = null) } }
            folderRows.value = emptyMap()
        }
    }
}
