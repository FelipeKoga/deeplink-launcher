package dev.koga.deeplinklauncher.deeplink.api.domain.mapper

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public suspend fun List<DeepLink>.toListItems(
    getDeepLinkHandlerIcon: GetDeepLinkHandlerIcon,
): List<DeepLinkListItem> = withContext(Dispatchers.Default) {
    map { deepLink ->
        DeepLinkListItem(
            deepLink = deepLink,
            icon = getDeepLinkHandlerIcon(deepLink.link),
        )
    }
}
