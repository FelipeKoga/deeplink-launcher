package dev.koga.deeplinklauncher.deeplink.api.repository

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import kotlinx.coroutines.flow.Flow

public interface FolderRepository {
    public fun getFoldersStream(): Flow<List<Folder>>
    public fun getFolders(): List<Folder>
    public fun getFolderDeepLinksStream(id: String): Flow<List<DeepLink>>
    public fun getFolderById(id: String): Folder?
    public fun upsertFolder(folder: Folder)
    public fun deleteFolder(id: String)
    public fun deleteAll()
}
