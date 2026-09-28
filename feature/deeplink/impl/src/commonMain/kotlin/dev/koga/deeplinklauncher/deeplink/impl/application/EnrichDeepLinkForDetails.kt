package dev.koga.deeplinklauncher.deeplink.impl.application

import dev.koga.deeplinklauncher.coroutines.AppDispatchers
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkHandlerInfo
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkMetadata
import kotlinx.coroutines.withContext

internal class EnrichDeepLinkForDetails(
    private val getDeepLinkHandlerIcon: GetDeepLinkHandlerIcon,
    private val getDeepLinkMetadata: GetDeepLinkMetadata,
    private val getDeepLinkHandlerInfo: GetDeepLinkHandlerInfo,
    private val dispatchers: AppDispatchers,
) {
    suspend operator fun invoke(deepLink: DeepLink): DeepLinkDetailsModel =
        withContext(dispatchers.io) {
            DeepLinkDetailsModel(
                deepLink = deepLink,
                icon = getDeepLinkHandlerIcon(deepLink.link, deepLink.targetPackage),
                metadata = getDeepLinkMetadata(deepLink.link),
                handlerInfo = getDeepLinkHandlerInfo(deepLink.link, deepLink.targetPackage),
            )
        }
}
