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

    /** Rejected, without changes, if another folder already uses the name. */
    public suspend fun insert(folder: Folder): InsertResult

    /** Rejected, without changes, if another folder already uses [name]. */
    public suspend fun update(id: String, name: String, description: String?): WriteResult

    /** Deletes the folder and unlinks its deeplinks. */
    public suspend fun delete(id: String)

    /** Deletes every folder and unlinks all deeplinks. */
    public suspend fun deleteAll()

    public sealed interface InsertResult {
        public data object Success : InsertResult
        public data object NameAlreadyExists : InsertResult
    }

    public sealed interface WriteResult {
        public data object Success : WriteResult
        public data object NameAlreadyExists : WriteResult
        public data object NotFound : WriteResult
    }
}
