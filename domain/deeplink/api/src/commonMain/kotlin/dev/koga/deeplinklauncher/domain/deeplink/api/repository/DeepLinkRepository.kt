package dev.koga.deeplinklauncher.domain.deeplink.api.repository
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.model.Folder
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

/**
 * Deeplink storage. Writes are commands scoped to one record and field, so a stale
 * [DeepLink] snapshot can never overwrite unrelated fields or another record.
 * A deeplink's folder is referenced by id only; folders are written through [FolderRepository].
 */
public interface DeepLinkRepository {
    public fun getDeepLinksStream(): Flow<List<DeepLink>>
    public fun getDeepLinks(): List<DeepLink>
    public fun getDeepLinkByIdStream(id: String): Flow<DeepLink?>
    public fun getDeepLinkById(id: String): DeepLink?
    public fun getDeepLinkByLink(link: String): DeepLink?

    /** Inserts [deepLink]. Its [DeepLink.folder] is stored by id; the folder itself is not written. */
    public suspend fun insert(deepLink: DeepLink): InsertResult

    /** Changes the link of [id]. Rejected, without changes, if another deeplink already uses [link]. */
    public suspend fun updateLink(id: String, link: String): WriteResult

    public suspend fun updateName(id: String, name: String?)
    public suspend fun updateDescription(id: String, description: String?)
    public suspend fun updateTargetPackage(id: String, targetPackage: String?)
    public suspend fun setFavorite(id: String, isFavorite: Boolean)
    public suspend fun setFolder(id: String, folderId: String?)
    public suspend fun recordLaunch(id: String, launchedAt: LocalDateTime)

    public suspend fun delete(id: String)

    /** Deletes every deeplink in one transaction and returns the deleted ids. */
    public suspend fun deleteAll(): List<String>

    /**
     * Applies an import in a single transaction: either everything is written or nothing is.
     *
     * - A folder whose name already belongs to another local folder is merged into that folder.
     * - A deeplink whose link already exists updates the existing record and keeps its local id;
     *   if the imported deeplink has no folder, the record keeps its current folder.
     * - Folder ids of the imported deeplinks refer to [folders] (or to existing local folders).
     * - A new deeplink whose id is taken by a different local record gets a new id.
     */
    public suspend fun importAll(folders: List<Folder>, deepLinks: List<DeepLink>)

    public sealed interface InsertResult {
        public data object Success : InsertResult
        public data object LinkAlreadyExists : InsertResult
    }

    public sealed interface WriteResult {
        public data object Success : WriteResult
        public data object LinkAlreadyExists : WriteResult
        public data object NotFound : WriteResult
    }
}
