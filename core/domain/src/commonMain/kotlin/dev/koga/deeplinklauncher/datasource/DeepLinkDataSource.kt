package dev.koga.deeplinklauncher.datasource

import dev.koga.deeplinklauncher.model.DeepLink
import kotlinx.coroutines.flow.Flow

interface DeepLinkDataSource {
    fun getDeepLinksStream(): Flow<List<DeepLink>>
    fun getDeepLinks(): List<DeepLink>
    fun getDeepLinkById(id: String): Flow<DeepLink?>
    fun getDeepLinkByLink(link: String): DeepLink?
    fun upsertDeepLink(deepLink: DeepLink)
    fun deleteDeepLink(id: String)
    fun deleteAll()
}
