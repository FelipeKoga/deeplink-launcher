package dev.koga.deeplinklauncher.deeplink.impl.application

import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinksForList
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class EnrichDeepLinksForListImpl(
    private val getDeepLinkHandlerIcon: GetDeepLinkHandlerIcon,
    private val getDeepLinkHandlerInfo: GetDeepLinkHandlerInfo,
) : EnrichDeepLinksForList {

    override suspend fun invoke(links: List<DeepLink>): List<DeepLinkListItem> =
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
