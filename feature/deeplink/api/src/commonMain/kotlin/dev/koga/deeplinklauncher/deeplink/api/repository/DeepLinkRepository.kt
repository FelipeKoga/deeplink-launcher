package dev.koga.deeplinklauncher.deeplink.api.repository

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import kotlinx.coroutines.flow.Flow

public interface DeepLinkRepository {
    public fun getDeepLinksStream(): Flow<List<DeepLink>>
    public fun getDeepLinks(): List<DeepLink>
    public fun getDeepLinkByIdStream(id: String): Flow<DeepLink?>
    public fun getDeepLinkById(id: String): DeepLink?
    public fun getDeepLinkByLink(link: String): DeepLink?
    public fun upsertDeepLink(deepLink: DeepLink)
    public fun deleteDeepLink(id: String)
    public fun deleteAll()
}
