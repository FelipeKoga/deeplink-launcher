package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkHandlerInfo

internal class GetDeepLinkHandlerInfoImpl : GetDeepLinkHandlerInfo {
    override fun invoke(link: String): DeepLinkHandlerInfo {
        return DeepLinkHandlerInfo(
            canResolve = false,
            appName = null,
        )
    }
}
