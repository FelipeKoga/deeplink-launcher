package dev.koga.deeplinklauncher.deeplink.api.repository

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import kotlinx.coroutines.flow.Flow

interface FolderRepository {
    fun getFoldersStream(): Flow<List<Folder>>
    fun getFolders(): List<Folder>
    fun getFolderDeepLinksStream(id: String): Flow<List<DeepLink>>
    fun getFolderById(id: String): Folder?
    fun upsertFolder(folder: Folder)
    fun deleteFolder(id: String)
    fun deleteAll()
}
