package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo

internal class GetDeepLinkHandlerInfoImpl : GetDeepLinkHandlerInfo {
    override fun invoke(link: String): DeepLinkHandlerInfo {
        return DeepLinkHandlerInfo(
            canResolve = false,
            appName = null,
        )
    }
}
