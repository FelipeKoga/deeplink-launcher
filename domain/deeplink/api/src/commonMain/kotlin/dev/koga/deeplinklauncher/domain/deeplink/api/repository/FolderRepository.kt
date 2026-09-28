package dev.koga.deeplinklauncher.domain.deeplink.api.repository
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.model.Folder
import kotlinx.coroutines.flow.Flow

public interface FolderRepository {
    public fun getFoldersStream(): Flow<List<Folder>>
    public fun getFolders(): List<Folder>
    public fun getFolderDeepLinksStream(id: String): Flow<List<DeepLink>>
    public fun getFolderByIdStream(id: String): Flow<Folder?>
    public fun getFolderById(id: String): Folder?

    public suspend fun insert(folder: Folder): InsertResult

    public suspend fun update(id: String, name: String, description: String?): WriteResult

    public suspend fun delete(id: String)

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
