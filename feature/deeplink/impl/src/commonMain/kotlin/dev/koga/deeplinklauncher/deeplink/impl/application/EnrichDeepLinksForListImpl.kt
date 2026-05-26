package dev.koga.deeplinklauncher.deeplink.impl.application

import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinksForList
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class EnrichDeepLinksForListImpl(
    private val getDeepLinkHandlerIcon: GetDeepLinkHandlerIcon,
    private val getDeepLinkHandlerInfo: GetDeepLinkHandlerInfo,
) : EnrichDeepLinksForList {

    override suspend fun invoke(links: List<DeepLink>): List<DeepLinkListItem> =
        withContext(Dispatchers.Default) {
            links.map { deepLink ->
                DeepLinkListItem(
                    deepLink = deepLink,
                    icon = getDeepLinkHandlerIcon(deepLink.link, deepLink.targetPackage),
                    handlerAppName = when (val handlerInfo =
                        getDeepLinkHandlerInfo(deepLink.link, deepLink.targetPackage)) {
                        is DeepLinkHandlerInfo.Available -> handlerInfo.appName
                        DeepLinkHandlerInfo.Unavailable -> null
                    },
                )
            }
        }
}
