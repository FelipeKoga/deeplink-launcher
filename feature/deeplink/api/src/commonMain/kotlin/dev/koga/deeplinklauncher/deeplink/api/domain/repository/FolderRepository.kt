package dev.koga.deeplinklauncher.deeplink.api.domain.repository

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import kotlinx.coroutines.flow.Flow

public interface FolderRepository {
    public fun getFoldersStream(): Flow<List<Folder>>
    public fun getFolders(): List<Folder>
    public fun getFolderDeepLinksStream(id: String): Flow<List<DeepLink>>
    public fun getFolderByIdStream(id: String): Flow<Folder?>
    public fun getFolderById(id: String): Folder?
    public fun upsertFolder(folder: Folder)
    public fun deleteFolder(id: String)
    public fun deleteAll()
}
