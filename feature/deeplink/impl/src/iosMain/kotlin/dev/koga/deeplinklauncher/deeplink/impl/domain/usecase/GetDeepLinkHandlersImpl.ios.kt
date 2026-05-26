package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandler
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlers

internal class GetDeepLinkHandlersImpl : GetDeepLinkHandlers {
    override suspend fun invoke(link: String): List<DeepLinkHandler> = emptyList()
}
