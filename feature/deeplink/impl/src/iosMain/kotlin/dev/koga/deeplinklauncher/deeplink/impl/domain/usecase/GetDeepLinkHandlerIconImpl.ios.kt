package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon

internal class GetDeepLinkHandlerIconImpl : GetDeepLinkHandlerIcon {
    override suspend fun invoke(link: String): ByteArray? = null
}
