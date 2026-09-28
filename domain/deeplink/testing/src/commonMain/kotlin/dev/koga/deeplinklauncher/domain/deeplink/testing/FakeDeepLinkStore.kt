@file:OptIn(ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.domain.deeplink.testing

import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public class FakeDeepLinkStore {
    private val state = MutableStateFlow(State())

    public val deepLinkRepository: DeepLinkRepository = FakeDeepLinkRepository()
    public val folderRepository: FolderRepository = FakeFolderRepository()

    private data class Row(val deepLink: DeepLink, val folderId: String?)

    private data class State(
        val rows: Map<String, Row> = emptyMap(),
        val folders: Map<String, Folder> = emptyMap(),
    ) {
        fun resolve(row: Row): DeepLink = row.deepLink.copy(
            folder = row.folderId?.let { id ->
                val folder = folders[id]
                Folder(id = id, name = folder?.name.orEmpty(), description = folder?.description)
            },
        )

        fun deepLinks(filter: (Row) -> Boolean = { true }): List<DeepLink> =
            rows.values.filter(filter).map(::resolve).sortedWith(
                compareByDescending<DeepLink> { it.lastLaunchedAt }
                    .thenByDescending { it.createdAt }
                    .thenBy { it.name },
            )

        fun foldersWithCounts(): List<Folder> =
            folders.values.map { folder -> folder.copy(deepLinkCount = rows.values.count { it.folderId == folder.id }) }

        fun rowByLink(link: String): Row? = rows.values.firstOrNull { it.deepLink.link == link }

        fun folderIdByName(name: String): String? = folders.values.firstOrNull { it.name == name }?.id

        fun updateRow(id: String, transform: (Row) -> Row): State =
            rows[id]?.let { copy(rows = rows + (id to transform(it))) } ?: this
    }

    private inner class FakeDeepLinkRepository : DeepLinkRepository {
        override fun getDeepLinksStream(): Flow<List<DeepLink>> = state.map { it.deepLinks() }

        override fun getDeepLinks(): List<DeepLink> = state.value.deepLinks()

        override fun getDeepLinkByIdStream(id: String): Flow<DeepLink?> =
            state.map { s -> s.rows[id]?.let(s::resolve) }

        override fun getDeepLinkById(id: String): DeepLink? = state.value.let { s -> s.rows[id]?.let(s::resolve) }

        override fun getDeepLinkByLink(link: String): DeepLink? =
            state.value.let { s -> s.rowByLink(link)?.let(s::resolve) }

        override suspend fun insert(deepLink: DeepLink): DeepLinkRepository.InsertResult {
            var result: DeepLinkRepository.InsertResult = DeepLinkRepository.InsertResult.Success
            state.update { s ->
                val owner = s.rowByLink(deepLink.link)
                if (owner != null && owner.deepLink.id != deepLink.id) {
                    result = DeepLinkRepository.InsertResult.LinkAlreadyExists
                    return@update s
                }
                check(deepLink.id !in s.rows) { "Duplicate id ${deepLink.id}" }
                result = DeepLinkRepository.InsertResult.Success
                s.copy(rows = s.rows + (deepLink.id to Row(deepLink.copy(folder = null), deepLink.folder?.id)))
            }
            return result
        }

        override suspend fun updateLink(id: String, link: String): DeepLinkRepository.WriteResult {
            var result: DeepLinkRepository.WriteResult = DeepLinkRepository.WriteResult.Success
            state.update { s ->
                val owner = s.rowByLink(link)
                when {
                    id !in s.rows -> s.also { result = DeepLinkRepository.WriteResult.NotFound }
                    owner != null && owner.deepLink.id != id ->
                        s.also { result = DeepLinkRepository.WriteResult.LinkAlreadyExists }

                    else -> s.updateRow(id) { it.copy(deepLink = it.deepLink.copy(link = link)) }
                        .also { result = DeepLinkRepository.WriteResult.Success }
                }
            }
            return result
        }

        override suspend fun updateName(id: String, name: String?) =
            state.update { s -> s.updateRow(id) { it.copy(deepLink = it.deepLink.copy(name = name)) } }

        override suspend fun updateDescription(id: String, description: String?) =
            state.update { s -> s.updateRow(id) { it.copy(deepLink = it.deepLink.copy(description = description)) } }

        override suspend fun updateTargetPackage(id: String, targetPackage: String?) =
            state.update { s -> s.updateRow(id) { it.copy(deepLink = it.deepLink.copy(targetPackage = targetPackage)) } }

        override suspend fun setFavorite(id: String, isFavorite: Boolean) =
            state.update { s -> s.updateRow(id) { it.copy(deepLink = it.deepLink.copy(isFavorite = isFavorite)) } }

        override suspend fun setFolder(id: String, folderId: String?) =
            state.update { s -> s.updateRow(id) { it.copy(folderId = folderId) } }

        override suspend fun recordLaunch(id: String, launchedAt: LocalDateTime) =
            state.update { s -> s.updateRow(id) { it.copy(deepLink = it.deepLink.copy(lastLaunchedAt = launchedAt)) } }

        override suspend fun delete(id: String) {
            state.update { s -> s.copy(rows = s.rows - id) }
        }

        override suspend fun deleteAll(): List<String> {
            var ids = emptyList<String>()
            state.update { s ->
                ids = s.rows.keys.toList()
                s.copy(rows = emptyMap())
            }
            return ids
        }

        override suspend fun importAll(folders: List<Folder>, deepLinks: List<DeepLink>) {
            state.update { s -> import(s, folders, deepLinks) }
        }

        private fun import(initial: State, folders: List<Folder>, deepLinks: List<DeepLink>): State {
            var s = initial
            val folderIds = mutableMapOf<String, String>()
            val deferred = mutableListOf<Folder>()

            fun putFolder(folder: Folder) {
                s = s.copy(folders = s.folders + (folder.id to folder.copy(deepLinkCount = 0)))
            }

            folders.forEach { folder ->
                val nameOwner = s.folderIdByName(folder.name)
                if (folder.id in s.folders && (nameOwner == null || nameOwner == folder.id)) {
                    putFolder(folder)
                    folderIds[folder.id] = folder.id
                } else {
                    deferred += folder
                }
            }
            deferred.forEach { folder ->
                val nameOwner = s.folderIdByName(folder.name)
                folderIds[folder.id] = nameOwner ?: folder.id.also { putFolder(folder) }
            }

            deepLinks.forEach { deepLink ->
                val importedFolderId = deepLink.folder?.id?.let { folderIds[it] ?: it.takeIf(s.folders::containsKey) }
                val existing = s.rowByLink(deepLink.link)
                s = if (existing != null) {
                    s.updateRow(existing.deepLink.id) {
                        Row(
                            deepLink = it.deepLink.copy(
                                name = deepLink.name,
                                description = deepLink.description,
                                createdAt = deepLink.createdAt,
                                isFavorite = deepLink.isFavorite,
                                targetPackage = deepLink.targetPackage,
                            ),
                            folderId = importedFolderId ?: it.folderId,
                        )
                    }
                } else {
                    val id = deepLink.id.takeUnless(s.rows::containsKey) ?: Uuid.random().toString()
                    s.copy(rows = s.rows + (id to Row(deepLink.copy(id = id, folder = null), importedFolderId)))
                }
            }
            return s
        }
    }

    private inner class FakeFolderRepository : FolderRepository {
        override fun getFoldersStream(): Flow<List<Folder>> = state.map { it.foldersWithCounts() }

        override fun getFolders(): List<Folder> = state.value.foldersWithCounts()

        override fun getFolderDeepLinksStream(id: String): Flow<List<DeepLink>> =
            state.map { s -> s.deepLinks { it.folderId == id } }

        override fun getFolderByIdStream(id: String): Flow<Folder?> =
            state.map { s -> s.foldersWithCounts().firstOrNull { it.id == id } }

        override fun getFolderById(id: String): Folder? = getFolders().firstOrNull { it.id == id }

        override suspend fun insert(folder: Folder): FolderRepository.InsertResult {
            var result: FolderRepository.InsertResult = FolderRepository.InsertResult.Success
            state.update { s ->
                val owner = s.folderIdByName(folder.name)
                if (owner != null && owner != folder.id) {
                    result = FolderRepository.InsertResult.NameAlreadyExists
                    return@update s
                }
                check(folder.id !in s.folders) { "Duplicate id ${folder.id}" }
                result = FolderRepository.InsertResult.Success
                s.copy(folders = s.folders + (folder.id to folder.copy(deepLinkCount = 0)))
            }
            return result
        }

        override suspend fun update(id: String, name: String, description: String?): FolderRepository.WriteResult {
            var result: FolderRepository.WriteResult = FolderRepository.WriteResult.Success
            state.update { s ->
                val owner = s.folderIdByName(name)
                when {
                    id !in s.folders -> s.also { result = FolderRepository.WriteResult.NotFound }
                    owner != null && owner != id -> s.also { result = FolderRepository.WriteResult.NameAlreadyExists }
                    else -> s.copy(
                        folders = s.folders + (id to s.folders.getValue(id).copy(name = name, description = description)),
                    ).also { result = FolderRepository.WriteResult.Success }
                }
            }
            return result
        }

        override suspend fun delete(id: String) {
            state.update { s ->
                State(
                    rows = s.rows.mapValues { (_, row) -> if (row.folderId == id) row.copy(folderId = null) else row },
                    folders = s.folders - id,
                )
            }
        }

        override suspend fun deleteAll() {
            state.update { s -> State(rows = s.rows.mapValues { (_, row) -> row.copy(folderId = null) }) }
        }
    }
}
