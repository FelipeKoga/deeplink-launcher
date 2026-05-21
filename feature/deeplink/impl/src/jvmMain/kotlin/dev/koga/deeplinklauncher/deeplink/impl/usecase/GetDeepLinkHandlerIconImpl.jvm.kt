package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkHandlerIcon

internal class GetDeepLinkHandlerIconImpl : GetDeepLinkHandlerIcon {
    override suspend fun invoke(link: String): ByteArray? = null
}
