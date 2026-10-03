package dev.koga.deeplinklauncher.deeplink.api.domain.repository

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

public interface DeepLinkRepository {
    public fun getDeepLinksStream(): Flow<List<DeepLink>>
    public fun getDeepLinks(): List<DeepLink>
    public fun getDeepLinkByIdStream(id: String): Flow<DeepLink?>
    public fun getDeepLinkById(id: String): DeepLink?
    public fun getDeepLinkByLink(link: String): DeepLink?
    public fun upsertDeepLink(deepLink: DeepLink): UpsertResult
    public fun updateLastLaunchedAt(id: String, lastLaunchedAt: LocalDateTime)
    public fun deleteDeepLink(id: String)
    public fun deleteAll()

    public sealed interface UpsertResult {
        public data object Saved : UpsertResult
        public data class LinkAlreadyExists(val existingId: String) : UpsertResult
    }
}
