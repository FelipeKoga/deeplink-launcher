package dev.koga.deeplinklauncher.deeplink.ui

import dev.koga.deeplinklauncher.deeplink.ui.model.DeepLinkListItem
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkHandlerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

/** Maps deeplinks to list items with their handler icon and app name, for every screen that lists links. */
public class EnrichDeepLinksForList(
    private val getDeepLinkHandlerIcon: GetDeepLinkHandlerIcon,
    private val getDeepLinkHandlerInfo: GetDeepLinkHandlerInfo,
) {

    public suspend operator fun invoke(links: List<DeepLink>): List<DeepLinkListItem> =
        withContext(enrichDispatcher) {
            if (links.size < PARALLEL_THRESHOLD) {
                links.map { enrich(it) }
            } else {
                links
                    .chunked((links.size + PARALLELISM - 1) / PARALLELISM)
                    .map { chunk -> async { chunk.map { enrich(it) } } }
                    .awaitAll()
                    .flatten()
            }
        }

    private suspend fun enrich(deepLink: DeepLink): DeepLinkListItem {
        currentCoroutineContext().ensureActive()
        return DeepLinkListItem(
            deepLink = deepLink,
            icon = getDeepLinkHandlerIcon(deepLink.link, deepLink.targetPackage),
            handlerAppName = when (
                val handlerInfo = getDeepLinkHandlerInfo(deepLink.link, deepLink.targetPackage)
            ) {
                is DeepLinkHandlerInfo.Available -> handlerInfo.appName
                DeepLinkHandlerInfo.Unavailable -> null
            },
        )
    }

    private companion object {
        const val PARALLELISM = 4
        const val PARALLEL_THRESHOLD = 16

        val enrichDispatcher = Dispatchers.IO.limitedParallelism(PARALLELISM)
    }
}
