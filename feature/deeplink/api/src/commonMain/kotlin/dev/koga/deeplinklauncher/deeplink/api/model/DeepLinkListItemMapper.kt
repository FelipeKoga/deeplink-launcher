package dev.koga.deeplinklauncher.deeplink.api.model

import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkHandlerIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public suspend fun List<DeepLink>.toListItems(
    getDeepLinkHandlerIcon: GetDeepLinkHandlerIcon,
): List<DeepLinkListItem> = withContext(Dispatchers.Default) {
    map { deepLink ->
        DeepLinkListItem(
            deepLink = deepLink,
            iconPng = getDeepLinkHandlerIcon(deepLink.link),
        )
    }
}
