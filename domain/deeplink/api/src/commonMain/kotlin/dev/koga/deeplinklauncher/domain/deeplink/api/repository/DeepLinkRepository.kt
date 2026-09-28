package dev.koga.deeplinklauncher.domain.deeplink.api.repository
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.model.Folder
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

public interface DeepLinkRepository {
    public fun getDeepLinksStream(): Flow<List<DeepLink>>
    public fun getDeepLinks(): List<DeepLink>
    public fun getDeepLinkByIdStream(id: String): Flow<DeepLink?>
    public fun getDeepLinkById(id: String): DeepLink?
    public fun getDeepLinkByLink(link: String): DeepLink?

    public suspend fun insert(deepLink: DeepLink): InsertResult

    public suspend fun updateLink(id: String, link: String): WriteResult

    public suspend fun updateName(id: String, name: String?)
    public suspend fun updateDescription(id: String, description: String?)
    public suspend fun updateTargetPackage(id: String, targetPackage: String?)
    public suspend fun setFavorite(id: String, isFavorite: Boolean)
    public suspend fun setFolder(id: String, folderId: String?)
    public suspend fun recordLaunch(id: String, launchedAt: LocalDateTime)

    public suspend fun delete(id: String)

    public suspend fun deleteAll(): List<String>

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
