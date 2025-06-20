package dev.koga.deeplinklauncher.deeplink.api.repository

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import kotlinx.coroutines.flow.Flow

interface DeepLinkRepository {
    fun getDeepLinksStream(): Flow<List<DeepLink>>
    fun getDeepLinks(): List<DeepLink>
    fun getDeepLinkByIdStream(id: String): Flow<DeepLink?>
    fun getDeepLinkById(id: String): DeepLink?
    fun getDeepLinkByLink(link: String): DeepLink?
    fun upsertDeepLink(deepLink: DeepLink)
    fun deleteDeepLink(id: String)
    fun deleteAll()
}
