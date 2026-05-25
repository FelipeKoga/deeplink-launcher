package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo

internal class GetDeepLinkHandlerInfoImpl : GetDeepLinkHandlerInfo {
    override suspend fun invoke(link: String): DeepLinkHandlerInfo {
        // iOS public APIs do not expose the target app display name required for handler info.
        return DeepLinkHandlerInfo.Unavailable
    }
}
