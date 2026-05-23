package dev.koga.deeplinklauncher.deeplink.impl.application

import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinkForDetails
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkDetailsModel

internal class EnrichDeepLinkForDetailsImpl(
    private val getDeepLinkHandlerIcon: GetDeepLinkHandlerIcon,
    private val getDeepLinkMetadata: GetDeepLinkMetadata,
    private val getDeepLinkHandlerInfo: GetDeepLinkHandlerInfo,
) : EnrichDeepLinkForDetails {
    override suspend fun invoke(deepLink: DeepLink): DeepLinkDetailsModel {
        return DeepLinkDetailsModel(
            deepLink = deepLink,
            icon = getDeepLinkHandlerIcon(deepLink.link),
            metadata = getDeepLinkMetadata(deepLink.link),
            handlerInfo = getDeepLinkHandlerInfo(deepLink.link),
        )
    }
}
