package dev.koga.deeplinklauncher.deeplink.impl.application

import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinksForList
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class EnrichDeepLinksForListImpl(
    private val getDeepLinkHandlerIcon: GetDeepLinkHandlerIcon,
    private val getDeepLinkHandlerInfo: GetDeepLinkHandlerInfo,
) : EnrichDeepLinksForList {
    private val cache = LinkedHashMap<String, DeepLinkListItem>()

    override suspend fun invoke(links: List<DeepLink>): List<DeepLinkListItem> = withContext(Dispatchers.Default) {
        links.map { deepLink ->
            val cacheKey = "${deepLink.id}:${deepLink.link}"
            val cached = cache[cacheKey]
            if (cached != null && cached.deepLink == deepLink) {
                return@map cached
            }

            DeepLinkListItem(
                deepLink = deepLink,
                icon = getDeepLinkHandlerIcon(deepLink.link),
                handlerAppName = getDeepLinkHandlerInfo(deepLink.link).appName,
                hasAssertion = deepLink.assertion?.hasCriteria == true,
            ).also { item ->
                cache[cacheKey] = item
                trimCacheIfNeeded()
            }
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
