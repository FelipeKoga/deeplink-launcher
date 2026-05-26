package dev.koga.deeplinklauncher.deeplink.impl.application

import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinksForList
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

internal class EnrichDeepLinksForListImpl(
    private val getDeepLinkHandlerIcon: GetDeepLinkHandlerIcon,
    private val getDeepLinkHandlerInfo: GetDeepLinkHandlerInfo,
) : EnrichDeepLinksForList {
    private val cache = LinkedHashMap<String, DeepLinkListItem>()
    private val cacheMutex = Mutex()

    override suspend fun invoke(links: List<DeepLink>): List<DeepLinkListItem> = withContext(Dispatchers.Default) {
        links.map { deepLink ->
            val cacheKey = "${deepLink.id}:${deepLink.link}:${deepLink.targetPackage.orEmpty()}"
            val cached = cacheMutex.withLock {
                cache[cacheKey]?.takeIf { it.deepLink == deepLink }
            }
            if (cached != null) {
                return@map cached
            }

            val item = DeepLinkListItem(
                deepLink = deepLink,
                icon = getDeepLinkHandlerIcon(deepLink.link, deepLink.targetPackage),
                handlerAppName = when (val handlerInfo = getDeepLinkHandlerInfo(deepLink.link, deepLink.targetPackage)) {
                    is DeepLinkHandlerInfo.Available -> handlerInfo.appName
                    DeepLinkHandlerInfo.Unavailable -> null
                },
            )
            cacheMutex.withLock {
                cache[cacheKey] = item
                trimCacheIfNeeded()
            }
            item
        }
    }

    private fun trimCacheIfNeeded() {
        while (cache.size > MAX_CACHE_SIZE) {
            val eldestKey = cache.keys.firstOrNull() ?: break
            cache.remove(eldestKey)
        }
    }

    companion object {
        private const val MAX_CACHE_SIZE = 256
    }
}
