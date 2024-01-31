package dev.koga.deeplinklauncher.datasource

import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import kotlinx.coroutines.flow.Flow

interface FolderDataSource {
    fun getFoldersStream(): Flow<List<Folder>>
    fun getFolders(): List<Folder>
    fun getFolderDeepLinksStream(id: String): Flow<List<DeepLink>>
    fun getFolderById(id: String): Folder?
    fun upsertFolder(folder: Folder)
    fun deleteFolder(id: String)
}
