package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkIcon
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon

internal class GetDeepLinkHandlerIconImpl : GetDeepLinkHandlerIcon {
    override suspend fun invoke(link: String): DeepLinkIcon? = null
}
